package com.example.yacht_backend.websocket;

import org.apache.tomcat.util.digester.DocumentProperties.Charset;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.NameValuePair;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.yacht_backend.dto.WebSocketMessage;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.repository.RoomRepository;
import com.example.yacht_backend.service.RoomDatabaseService;
import com.example.yacht_backend.service.RoomService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import jakarta.persistence.EntityNotFoundException;

/*

대략적 flow

1. host에서 연결을 establish함. 이건 host가 room을 유지하는한 반영구적.
2. guest에서 /enter-room으로 입장 요청을 날림. 이 때 guest는 연결을 establish하고, host에게 연결 요청을 전달함.
3-1. guest연결이 먼저 이루어지고, host에서 답이 오는 경우(혹은 host에서 응답하지 않는 경우)
    1) guest는 연결과 동시에 60초 timeout schedular를 등록. expire시 timeout과 함께 연결 종료.
    2) host가 입장 요청에 대한 응답이 날아오면, 이건 즉시 이미 연결된 guest를 통해 응답을 돌려주고, host에도 동일한 응답을 돌려줌.
    3) guest와 연결 종료.
3-2. host에서 답이 먼저 오고, guest 연결이 이루어지는 경우(혹은 guest에서 응답하지 않는 경우).
    1) host는 pendingRequests에 guestId를 등록 후 60초 timeout schedular를 등록. expire시 host에 timeout 응답을 돌려줌.
        (expire 이후 guest의 연결이 진행되어도 자동으로 timeout처리됨.)
    2) guest의 연결이 이루어지면 pendingRequests에서 요청에 대한 답을 보고 즉시 응답 후 연결 종료. 연결 성공시 host에도 응답을 함.

*/

public class WebSocketHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, String> sessionUserMap  = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, WebSocketSession> userSessionMap  = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> sessionTimeouts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> pendingRequests  = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ReentrantLock lock = new ReentrantLock();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final RoomDatabaseService roomDatabaseService;

    WebSocketHandler(RoomDatabaseService roomDatabaseService) {
        this.roomDatabaseService = roomDatabaseService;
    }

    static HashMap<String, String> parseQuerystring(String query) throws IllegalArgumentException {
        List<NameValuePair> params = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
        HashMap<String, String> queryPairs = new HashMap<>();
        for (NameValuePair param : params) {
            String name = param.getName();
            String value = param.getValue();

            queryPairs.put(name, value);
        }
        return queryPairs;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String query = session.getUri().getQuery();
        if (query == null) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        
        HashMap<String, String> queryMap;
        try {
            queryMap = parseQuerystring(query);
        }
        catch (IllegalArgumentException e) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        String user = queryMap.get("user");
        String hostUserId = queryMap.get("hostUserId");
        String guestUserId = queryMap.get("guestUserId");
        if ((user != "host" && user != "guest") ||
            (user == "host" && hostUserId == null) ||
            (user == "guest" && (hostUserId == null || guestUserId == null))) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        
        if (user == "host") {
            handleHostConnection(session, hostUserId);
        }
        else if (user == "guest") {
            handleGuestConnection(session, hostUserId, guestUserId);
        }
    }

    void handleHostConnection(WebSocketSession session, String hostUserId) throws Exception {
        if (roomDatabaseService.findRoomByHostUserId(hostUserId) == null) {
            session.close(CloseStatus.SERVER_ERROR);
            return;
        }

        lock.lock();
        sessionUserMap.put(session.getId(), hostUserId);
        userSessionMap.put(hostUserId, session);
        lock.unlock();
    }

    void handleGuestConnection(WebSocketSession session, String hostUserId, String guestUserId) throws Exception {
        if (roomDatabaseService.findRoomByHostUserId(hostUserId) == null) {
            session.close(CloseStatus.SERVER_ERROR);
            return;
        }
        lock.lock();
        sessionUserMap.put(session.getId(), guestUserId);
        userSessionMap.put(guestUserId, session);

        String requestHostId = pendingRequests.get(guestUserId);
        
        if (requestHostId == null) {
            scheduleGuestTimeout(session, hostUserId, guestUserId);
        }
        else if (requestHostId == guestUserId) {
            // host로부터 이미 거절요청을 받음.
            TextMessage message = new WebSocketMessage(hostUserId, guestUserId, WebSocketMessage.REJECTED).toTextMessage();
            session.sendMessage(message);
            session.close();
        }
        else {
            // host로부터 이미 수락요청을 받음.
            WebSocketSession hostSession = userSessionMap.get(hostUserId);
            TextMessage message = new WebSocketMessage(hostUserId, guestUserId, WebSocketMessage.ACCEPTED).toTextMessage();
            session.sendMessage(message);
            hostSession.sendMessage(message);
            roomDatabaseService.addGuestUserToRoom(hostUserId, guestUserId);
            session.close();
        }
        lock.unlock();
    }

    void scheduleGuestTimeout(WebSocketSession session, String hostUserId, String guestUserId) {
        ScheduledFuture<?> timeout = scheduler.schedule(() -> {
            try {
                if (session.isOpen()) {
                    TextMessage message = new WebSocketMessage(hostUserId, guestUserId, WebSocketMessage.TIMEOUT).toTextMessage();
                    session.sendMessage(message);
                    session.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.MINUTES);
        sessionTimeouts.put(session.getId(), timeout);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String payload = message.getPayload();
        WebSocketMessage data = objectMapper.readValue(payload, WebSocketMessage.class);

        String hostUserId = data.getHostUserId();
        String guestUserId = data.getGuestUserId();
        int acceptEnter = data.getAcceptEnter();

        if (hostUserId == null || guestUserId == null || acceptEnter == WebSocketMessage.INVALID) {
            return;
        }

        lock.lock();

        String userId = sessionUserMap.get(session.getId());

        if (userId != hostUserId) {
            lock.unlock();
            return;
        }

        if (acceptEnter == WebSocketMessage.ACCEPTED) {
            WebSocketSession guestUserSession = userSessionMap.get(guestUserId);
            if (guestUserSession == null) {
                pendingRequests.put(guestUserId, hostUserId);
                scheduleHostTimeout(session, hostUserId, guestUserId);
            }
            else {
                TextMessage responseMessage = new WebSocketMessage(hostUserId, guestUserId, WebSocketMessage.ACCEPTED).toTextMessage();
                session.sendMessage(responseMessage);
                guestUserSession.sendMessage(responseMessage);
                roomDatabaseService.addGuestUserToRoom(hostUserId, guestUserId);
            }
        }
        else if (acceptEnter == WebSocketMessage.REJECTED) {
            WebSocketSession guestUserSession = userSessionMap.get(guestUserId);
            if (guestUserSession == null) {
                pendingRequests.put(guestUserId, guestUserId);
                scheduleHostTimeout(session, hostUserId, guestUserId);
            }
            else {
                TextMessage responseMessage = new WebSocketMessage(hostUserId, guestUserId, WebSocketMessage.REJECTED).toTextMessage();
                guestUserSession.sendMessage(responseMessage);
            }
        }
        lock.unlock();
    }

    void scheduleHostTimeout(WebSocketSession session, String hostUserId, String guestUserId) {
        ScheduledFuture<?> timeout = scheduler.schedule(() -> {
            lock.lock();
            String prevResponse = pendingRequests.remove(guestUserId);
            if (prevResponse != null && prevResponse != guestUserId) {
                try {
                    if (session.isOpen()) {
                        TextMessage message = new WebSocketMessage(hostUserId, guestUserId, WebSocketMessage.TIMEOUT).toTextMessage();
                        session.sendMessage(message);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }, 1, TimeUnit.MINUTES);
        sessionTimeouts.put(session.getId(), timeout);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        String sessionId = session.getId();
        lock.lock();
        String userId = sessionUserMap.get(sessionId);
        sessionUserMap.remove(sessionId);
        userSessionMap.remove(userId);
        ScheduledFuture<?> timeout = sessionTimeouts.remove(sessionId);
        if (timeout != null) {
            timeout.cancel(true);
        }
        lock.unlock();
    }

    public static boolean enterRoom(String hostUserId, String guestUserId) throws Exception {
        lock.lock();
        WebSocketSession session = userSessionMap.get(hostUserId);
        TextMessage message = new WebSocketMessage(hostUserId, guestUserId, WebSocketMessage.ACCEPTED).toTextMessage();
        if (session == null) {
            lock.unlock();
            return false;
        }
        
        session.sendMessage(message);
        lock.unlock();
        return true;
    }
}
