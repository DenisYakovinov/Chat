package ru.job4j.chat.exception;

public class ServiceValidateException extends ServiceException {

    public ServiceValidateException(String message) {
        super(message);
    }

    public ServiceValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}