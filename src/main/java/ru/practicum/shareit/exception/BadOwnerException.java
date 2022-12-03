package ru.practicum.shareit.exception;

public class BadOwnerException extends RuntimeException {
    public BadOwnerException(String message) {
        super(message);
    }
}
