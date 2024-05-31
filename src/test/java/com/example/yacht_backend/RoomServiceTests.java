package com.example.yacht_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.yacht_backend.service.RoomService;
import com.example.yacht_backend.service.RoomDatabaseService;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.model.Room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import java.util.List;
import java.util.Collections;
import java.util.UUID;

class RoomServiceTests {
	@InjectMocks
	private RoomService roomService;

    @Mock
    private RoomDatabaseService roomDatabaseService;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	void testGetAllRooms() throws Exception {
		given(roomDatabaseService.findAll()).willReturn(Collections.emptyList());

        List<Room> allRooms = roomService.getAllRooms();

        assertEquals(allRooms, Collections.emptyList());
	}

	@Test
	void testCreateNewRoom() throws Exception {
        UUID roomId = UUID.randomUUID();
		String userId = UUID.randomUUID().toString();
		Room newRoom = new Room(roomId.toString(), userId, null);

		mockStatic(UUID.class);

		given(roomDatabaseService.findRoomByHostUserId(userId)).willReturn(null);
		given(roomDatabaseService.findRoomByGuestUserId(userId)).willReturn(null);
		given(roomDatabaseService.save(newRoom)).willReturn(newRoom);
		given(UUID.randomUUID()).willReturn(roomId);

		CreateNewRoomResponse createNewRoomResponse = roomService.createNewRoom(userId);

		assertEquals(createNewRoomResponse.getRoomId(), roomId.toString());
	}

}
