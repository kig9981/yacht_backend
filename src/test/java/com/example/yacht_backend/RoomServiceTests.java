package com.example.yacht_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.yacht_backend.service.RoomService;
import com.example.yacht_backend.service.RoomDatabaseService;
import com.example.yacht_backend.service.UserDatabaseService;
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

	@Mock
    private UserDatabaseService userDatabaseService;

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
		String sessionId = UUID.randomUUID().toString();
		String userId = UUID.randomUUID().toString();
		String hostData = "data";

		mockStatic(UUID.class);

		given(userDatabaseService.findUserIdBySessionId(sessionId)).willReturn(userId);
		given(roomDatabaseService.findRoomByHostUserId(userId)).willReturn(null);
		given(roomDatabaseService.findRoomByGuestUserId(userId)).willReturn(null);
		given(UUID.randomUUID()).willReturn(roomId);

		CreateNewRoomResponse createNewRoomResponse = roomService.createNewRoom(userId, hostData);

		assertEquals(createNewRoomResponse.getRoomId(), roomId.toString());
	}

}
