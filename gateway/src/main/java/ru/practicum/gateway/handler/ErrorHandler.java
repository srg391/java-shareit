package ru.practicum.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.invoke.MissingParametersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({MissingRequestHeaderException.class,
            MissingServletRequestParameterException.class,
            MissingParametersException.class,
            ConstraintViolationException.class,
            UnexpectedTypeException.class,
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationException(Exception e) {
        Map<String, String> errors = Map.of("error", e.getMessage());
        log.debug(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
