package com.example.yacht_backend.exception;

public class RoomNotFoundException extends Exception {
    public RoomNotFoundException() {
        super();
    }

    public RoomNotFoundException(String message) {
        super(message);
    }

    public RoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoomNotFoundException(Throwable cause) {
        super(cause);
    }
}
