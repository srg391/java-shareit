package ru.practicum.shareit.exception;

public class BadStatusForCommentException extends RuntimeException {
    public BadStatusForCommentException(String message) {
        super(message);
    }
}
