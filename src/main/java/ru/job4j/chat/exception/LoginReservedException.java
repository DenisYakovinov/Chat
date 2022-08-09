package ru.job4j.chat.exception;

public class LoginReservedException extends ServiceException {

    public LoginReservedException(String message) {
        super(message);
    }

    public LoginReservedException(String message, Throwable cause) {
        super(message, cause);
    }
}