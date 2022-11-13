package ru.practicum.shareit.exception;

public class BadStatusBookingException extends RuntimeException {
    public BadStatusBookingException(String message) {
        super(message);
    }
}
