package ru.job4j.chat.exception;

public class RoomNameReservedException extends ServiceException {

    public RoomNameReservedException(String message) {
        super(message);
    }

    public RoomNameReservedException(String message, Throwable cause) {
        super(message, cause);
    }
}