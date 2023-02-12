package ru.practicum.gateway.booking.validator;

import ru.practicum.gateway.booking.dto.BookingItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingItemRequestDto> {

    @Override
    public boolean isValid(BookingItemRequestDto dto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime currentDate = LocalDateTime.now();

        return (dto.getStart().isAfter(currentDate) || dto.getStart().isEqual(currentDate))
                && (dto.getEnd().isAfter(currentDate) || dto.getEnd().isEqual(currentDate))
                && (dto.getStart().isBefore(dto.getEnd()) || dto.getEnd().isEqual(currentDate));
    }
}
