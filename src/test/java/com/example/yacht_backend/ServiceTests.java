package com.example.yacht_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.yacht_backend.service.ApiService;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.repository.RoomRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import java.util.List;
import java.util.Collections;
import java.util.UUID;

class ServiceTests {
	@InjectMocks
	private ApiService apiService;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	void testGetAllRooms() throws Exception {
		given(roomRepository.findAll()).willReturn(Collections.emptyList());

        List<Room> allRooms = apiService.getAllRooms();

        assertEquals(allRooms, Collections.emptyList());
	}

	@Test
	void testCreateNewRoom() throws Exception {
        UUID roomId = UUID.randomUUID();
		String userId = UUID.randomUUID().toString();
		Room newRoom = new Room(roomId.toString(), userId, null);

		mockStatic(UUID.class);

		given(roomRepository.save(newRoom)).willReturn(newRoom);
		given(UUID.randomUUID()).willReturn(roomId);

		String newRoomId = apiService.createNewRoom(userId);

		assertEquals(newRoomId, roomId.toString());
	}

}
