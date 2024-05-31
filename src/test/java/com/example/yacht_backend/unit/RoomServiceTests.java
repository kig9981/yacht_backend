package com.example.yacht_backend.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.web.context.request.async.DeferredResult;

import com.example.yacht_backend.service.RoomService;
import com.example.yacht_backend.service.RoomDatabaseService;
import com.example.yacht_backend.service.UserDatabaseService;
import com.example.yacht_backend.domain.RoomData;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.model.PendingRoom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import java.util.List;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class RoomServiceTests {
	@InjectMocks
	private RoomService roomService;

    @Mock
    private RoomDatabaseService roomDatabaseService;

	@Mock
    private UserDatabaseService userDatabaseService;

	@Mock
	private ConcurrentHashMap<String, RoomData> roomGuestMap;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	void testGetAllRooms() throws Exception {
		given(roomDatabaseService.findAllPendingRoom()).willReturn(Collections.emptyList());

        List<PendingRoom> allRooms = roomService.getAllRooms();

        assertEquals(Collections.emptyList(), allRooms);
	}

	@Test
	void testCreateNewRoom() throws Exception {
        UUID roomId = UUID.randomUUID();
		String sessionId = UUID.randomUUID().toString();
		String userId = UUID.randomUUID().toString();
		String hostData = "data";

		mockStatic(UUID.class);

		given(userDatabaseService.findUserIdBySessionId(sessionId)).willReturn(userId);
		given(roomDatabaseService.findActiveRoomByGuestUserId(userId)).willReturn(null);
		given(UUID.randomUUID()).willReturn(roomId);

		CreateNewRoomResponse createNewRoomResponse = roomService.createNewRoom(userId, hostData);

		assertEquals(roomId.toString(), createNewRoomResponse.getRoomId());
	}

	@Test
	void testEnterRoom() throws Exception {
		String roomId = UUID.randomUUID().toString();
		String sessionId = UUID.randomUUID().toString();
		String hostUserId = UUID.randomUUID().toString();
		String guestUserId = UUID.randomUUID().toString();
		DeferredResult<String> deferredGuestUserId = new DeferredResult<>();
		DeferredResult<String> deferredGuestUserData = new DeferredResult<>();
		RoomData roomData = new RoomData(roomId, hostUserId, "hostUserData", deferredGuestUserId, deferredGuestUserData);

		given(userDatabaseService.findUserIdBySessionId(sessionId)).willReturn(guestUserId);
		given(roomDatabaseService.isUserInRoom(guestUserId)).willReturn(false);
		given(roomGuestMap.get(roomId)).willReturn(roomData);

		EnterRoomResponse enterRoomResponse = roomService.enterRoom(roomId, sessionId, "guestData");
		
		assertEquals(hostUserId, enterRoomResponse.getResponse());
		assertEquals(true, enterRoomResponse.getIsAccepted());
	}

}
