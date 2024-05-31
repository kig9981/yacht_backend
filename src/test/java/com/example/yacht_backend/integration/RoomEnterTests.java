package com.example.yacht_backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import com.example.yacht_backend.domain.RoomData;
import com.example.yacht_backend.dto.CreateNewRoomRequest;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.model.PendingRoom;
import com.example.yacht_backend.model.User;
import com.example.yacht_backend.service.RoomDatabaseService;
import com.example.yacht_backend.service.RoomService;
import com.example.yacht_backend.service.UserDatabaseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class RoomEnterTests {
    private static final Logger logger = LoggerFactory.getLogger(RoomEnterTests.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomDatabaseService roomDatabaseService;

	@Autowired
    private UserDatabaseService userDatabaseService;

	@Autowired
	private ConcurrentHashMap<String, RoomData> roomGuestMap;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateNewRoom() throws Exception {
        // 유저 생성
        ResponseEntity<User> response1 = restTemplate.postForEntity("/users", null, User.class);

        assertEquals(HttpStatus.OK, response1.getStatusCode());

        User user = response1.getBody();

        assertNotNull(user);

        logger.info(user.getSessionId());
        logger.info(user.getUserId());

        logger.info("" + userDatabaseService.findAll().size());

        // 방 생성

        CreateNewRoomRequest createNewRoomRequest = new CreateNewRoomRequest(user.getSessionId(), "data");
        roomService.setDefferedResultTimeout(50000L);
        roomService.enableOnCompletion(false);

        // MvcResult mvcResult = mockMvc.perform(post("/rooms/wait")
        //     .contentType(MediaType.APPLICATION_JSON)
		// 	.content(objectMapper.writeValueAsString(createNewRoomRequest)))
        //     .andExpect(request().asyncStarted())
        //     .andReturn();

        // mockMvc.perform(asyncDispatch(mvcResult))
        //     .andExpect(status().isOk())
        //     .andExpect(jsonPath("$.roomId").exists())
        //     .andExpect(jsonPath("$.guestUserId").exists())
        //     .andExpect(jsonPath("$.data").exists());

        // CreateNewRoomResponse createNewRoom = response2.getBody();

        // assertNotNull(createNewRoom);

        // String roomId = createNewRoom.getRoomId();
        // DeferredResult<String> guestUserId = createNewRoom.getGuestUserId();
        // DeferredResult<String> data = createNewRoom.getData();
        // PendingRoom pendingRoom = roomDatabaseService.findPendingRoomById(roomId);

        // assertNotNull(pendingRoom);
        // assertFalse(guestUserId.hasResult());
        // assertFalse(data.hasResult());
        // assertEquals(roomGuestMap.size(), 1);

        // TimeUnit.SECONDS.sleep(5);

        // pendingRoom = roomDatabaseService.findPendingRoomById(roomId);

        // assertNull(pendingRoom);
        // assertTrue(guestUserId.hasResult());
        // assertTrue(data.hasResult());
        // assertEquals(roomGuestMap.size(), 0);


    }
    
}
