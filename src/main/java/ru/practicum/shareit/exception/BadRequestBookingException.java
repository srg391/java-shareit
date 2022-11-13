package ru.practicum.shareit.exception;

public class BadRequestBookingException extends RuntimeException {
    public BadRequestBookingException(String message) {
        super(message);
    }
}
