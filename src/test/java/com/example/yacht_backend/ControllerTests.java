package com.example.yacht_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.yacht_backend.dto.CreateNewRoomRequest;
import com.example.yacht_backend.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Collections;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ApiService apiService;

	@Autowired
    private ObjectMapper objectMapper;

	@Test
	void testGetAllRooms() throws Exception {
		given(apiService.getAllRooms()).willReturn(Collections.emptyList());

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
		CreateNewRoomRequest createNewRoomRequest = new CreateNewRoomRequest(userId.toString());
		given(apiService.createNewRoom(createNewRoomRequest.getUserId())).willReturn(roomId.toString());

		mockMvc.perform(post("/create-new-room")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createNewRoomRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.roomId").value(roomId.toString()))
			.andDo(print());
	}

}
