package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({NullPointerException.class,
            NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(Exception e) {
        return Map.of("Error 404", e.getMessage());
    }

    @ExceptionHandler({MissingRequestHeaderException.class,
            ConstraintViolationException.class,
            UnexpectedTypeException.class,
            MethodArgumentNotValidException.class,
            ItemIsNotAvailableException.class,
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(Exception e) {
        return Map.of("error", e.getMessage());
    }

}
