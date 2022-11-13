package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingMapper {

    public BookingDto createDtoBooking(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.Booker(booking.getBooker().getId()),
                booking.getStatus()
        );
    }

    public List<BookingDto> createDtoListBooking(List<Booking> bookings) {
        return bookings.stream()
                .map(this::createDtoBooking)
                .collect(Collectors.toList());
    }

    public Booking createBooking(StartAndEndBookingDto startAndEndBookingDto, Item item, User user) {
        return new Booking(
                null,
                startAndEndBookingDto.getStart(),
                startAndEndBookingDto.getEnd(),
                item,
                user,
                BookingStatus.WAITING
        );
    }
}
