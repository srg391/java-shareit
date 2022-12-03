package ru.practicum.shareit.exception;

public class BadStatusForBookingsOfOwnerException extends RuntimeException {
    public BadStatusForBookingsOfOwnerException(String message) {
        super(message);
    }
}
