package com.example.yacht_backend.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.yacht_backend.dto.CreateNewRoomRequest;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomRequest;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoomService roomService;

	@Autowired
    private ObjectMapper objectMapper;

	@Test
	void testGetAllRooms() throws Exception {
		given(roomService.getAllRooms()).willReturn(Collections.emptyList());

		mockMvc.perform(get("/rooms")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isEmpty());
			// .andDo(print());
	}

	@Test
	void testCreateNewRoom() throws Exception {
		UUID userId = UUID.randomUUID();
		UUID roomId = UUID.randomUUID();
		UUID guestId = UUID.randomUUID();
		DeferredResult<String> deferredGuestId = new DeferredResult<>();
		DeferredResult<String> deferredData = new DeferredResult<>();
		deferredGuestId.setResult(guestId.toString());
		deferredData.setResult("data");
		CreateNewRoomRequest createNewRoomRequest = new CreateNewRoomRequest(userId.toString(), "data");
		CreateNewRoomResponse createNewRoomResponse = new CreateNewRoomResponse(roomId.toString(), deferredGuestId, deferredData);
		given(roomService.createNewRoom(createNewRoomRequest.getSessionId(), "data")).willReturn(createNewRoomResponse);

		mockMvc.perform(post("/rooms/wait")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createNewRoomRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.roomId").value(roomId.toString()));
			// .andDo(print());
			
	}

	@Test
	void testEnterRoom() throws Exception {
		UUID roomId = UUID.randomUUID();
		UUID sessionId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		EnterRoomRequest enterRoomRequest = new EnterRoomRequest(sessionId.toString(), "data");
		EnterRoomResponse enterRoomResponse = new EnterRoomResponse("", true);

		given(roomService.enterRoom(roomId.toString(), sessionId.toString(), "data")).willReturn(enterRoomResponse);

		mockMvc.perform(post("/rooms/" + roomId.toString() + "/enter")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(enterRoomRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isAccepted").value(true))
			.andExpect(jsonPath("$.response").value(""));
			// .andDo(print());

	}

}
