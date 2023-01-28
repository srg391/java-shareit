package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {
    private BookingMapper bookingMapper;

    CommentMapper commentMapper;

    ItemMapper itemMapper;

    private User user1;

    private Item item1;

    private Booking booking1;

    private Booking booking2;

    @BeforeEach
    void beforeEach() {
        commentMapper = new CommentMapper();
        itemMapper = new ItemMapper(commentMapper);
        bookingMapper = new BookingMapper();
        user1 = new User(1L, "Sergey1", "sergey1@gmail.com");
        item1 = new Item(1L, "вещь", "описание вещи", true, user1, 1L);
        booking1 = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), item1, user1, BookingStatus.APPROVED);
        booking2 = new Booking(2L, LocalDateTime.of(2022, 11, 10, 11, 55), LocalDateTime.of(2022, 12, 3, 12, 55), item1, user1, BookingStatus.CANCELED);
    }

    @Test
    void createBooking() {

        StartAndEndBookingDto bookingDto = new StartAndEndBookingDto(LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), 1L);
        Booking bookingNew = bookingMapper.createBooking(bookingDto, item1, user1);
        assertEquals(bookingNew.getEnd(), booking1.getEnd());
    }

    @Test
    void createBookingDto() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.of(2021, 11, 3, 9, 55),
                LocalDateTime.of(2022, 11, 8, 19, 55), new BookingDto.Item(1L, "вещь"), new BookingDto.Booker(1L), BookingStatus.APPROVED);
        BookingDto bookingDtoNew = bookingMapper.createDtoBooking(booking1);
        assertEquals(bookingDtoNew, bookingDto);
    }

    @Test
    void createDtoListBookingTest() {
        BookingDto bookingDto1 = new BookingDto(1L, LocalDateTime.of(2021, 11, 3, 9, 55),
                LocalDateTime.of(2022, 11, 8, 19, 55), new BookingDto.Item(1L, "вещь"), new BookingDto.Booker(1L), BookingStatus.APPROVED);
        BookingDto bookingDto2 = new BookingDto(2L, LocalDateTime.of(2022, 11, 10, 11, 55), LocalDateTime.of(2022, 12, 3, 12, 55),new BookingDto.Item(1L, "вещь"), new BookingDto.Booker(1L), BookingStatus.APPROVED);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

        List<BookingDto> bookingsDtoNew = new ArrayList<>();
        bookingsDtoNew.add(bookingDto1);
        bookingsDtoNew.add(bookingDto2);

        List<BookingDto> bookingsDto = bookingMapper.createDtoListBooking(bookings);

        assertEquals(bookingsDto, bookingsDtoNew);
    }

}
