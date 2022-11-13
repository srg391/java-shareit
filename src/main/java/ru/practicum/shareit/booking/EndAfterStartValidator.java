package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, StartAndEndBookingDto> {

    @Override
    public boolean isValid(StartAndEndBookingDto startAndEndBookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime currentDate = LocalDateTime.now();


        return (startAndEndBookingDto.getStart().isAfter(currentDate) || startAndEndBookingDto.getStart().isEqual(currentDate))
                && (startAndEndBookingDto.getEnd().isAfter(currentDate) || startAndEndBookingDto.getEnd().isEqual(currentDate))
                && (startAndEndBookingDto.getStart().isBefore(startAndEndBookingDto.getEnd()) || startAndEndBookingDto.getEnd().isEqual(currentDate));
    }
}