package com.example.yacht_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.yacht_backend.dto.CreateNewRoomRequest;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
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

		mockMvc.perform(get("/get-all-rooms")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isEmpty())
			.andDo(print());
	}

	@Test
	void testCreateNewRoom() throws Exception {
		UUID userId = UUID.randomUUID();
		UUID roomId = UUID.randomUUID();
		UUID guestId = UUID.randomUUID();
		DeferredResult<String> deferredGuestId = new DeferredResult<>();
		deferredGuestId.setResult(guestId.toString());
		CreateNewRoomRequest createNewRoomRequest = new CreateNewRoomRequest(userId.toString());
		CreateNewRoomResponse createNewRoomResponse = new CreateNewRoomResponse(roomId.toString(), deferredGuestId);
		given(roomService.createNewRoom(createNewRoomRequest.getSessionId())).willReturn(createNewRoomResponse);

		mockMvc.perform(post("/create-new-room")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createNewRoomRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.roomId").value(roomId.toString()))
			.andDo(print());
			
	}

}
