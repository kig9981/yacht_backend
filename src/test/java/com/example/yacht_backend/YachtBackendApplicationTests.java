package com.example.yacht_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.yacht_backend.service.ApiService;

import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
class YachtBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ApiService apiService;

	@Test
	void testGetAllRooms() throws Exception {
		given(apiService.getAllRooms()).willReturn(Collections.emptyList());

		this.mockMvc.perform(get("/get-all-rooms")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isEmpty())
			.andDo(print());
	}

}
