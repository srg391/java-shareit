package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;
import ru.practicum.shareit.exception.ItemNotAvailableException;

import java.util.List;

public interface BookingService {
    public BookingDto getBooking(long userId, long bookingId);

    public List<BookingDto> getAllBookingsOfUser(long userId, BookingState state, int from, int size);

    public List<BookingDto> getAllBookingsOfOwner(long userId, BookingState state, int from, int size);

    public BookingDto createBooking(long userId, StartAndEndBookingDto startAndEndBookingDto) throws ItemNotAvailableException;

    public BookingDto updateBooking(long userId, long bookingId, boolean approved);
}
