package ru.practicum.shareit.booking.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class BookingJsonTest {
    private BookingMapper bookingMapper;
    @Autowired
    private JacksonTester<BookingDto> json;

    @SneakyThrows
    @Test
    void createDtoBooking() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.of(2021, 11, 3, 9, 55),
                LocalDateTime.of(2022, 11, 8, 19, 55), new BookingDto.Item(1L, "вещь"), new BookingDto.Booker(1L), BookingStatus.APPROVED);
        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo(bookingDto.getStatus().toString());
    }

    @Test
    void createDtoListBooking() {
        bookingMapper = new BookingMapper();
        User user = new User(1L, "Sergey1", "sergey1@gmail.com");
        User user1 = new User(2L, "Sergey2", "sergey2@gmail.com");
        Item item = new Item(1L, "вещь", "описание вещи", true, user, 1L);
        Item item1 = new Item(2L, "предмет", "описание предмета", true, user1, 2L);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.of(2021, 11, 3, 9, 55));
        bookingDto.setEnd(LocalDateTime.of(2022, 11, 8, 19, 55));
        bookingDto.setItem(new BookingDto.Item(1L, "вещь"));
        bookingDto.setBooker(new BookingDto.Booker(1L));
        bookingDto.setStatus(BookingStatus.APPROVED);

        BookingDto bookingDto1 = new BookingDto(2L, LocalDateTime.of(2021, 11, 3, 9, 55),
                LocalDateTime.of(2022, 11, 8, 19, 55), new BookingDto.Item(1L, "предмет"), new BookingDto.Booker(2L), BookingStatus.APPROVED);

        List<BookingDto> bookingsDto = new ArrayList<>();

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2021, 11, 3, 9, 55));
        booking.setEnd(LocalDateTime.of(2022, 11, 8, 19, 55));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        Booking booking1 = new Booking(2L, LocalDateTime.of(2022, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), item1, user1, BookingStatus.APPROVED);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking1);

        BookingDto bookingDtoNew = bookingMapper.createDtoBooking(booking);
        BookingDto bookingDtoNew1 = bookingMapper.createDtoBooking(booking1);

        List<BookingDto> bookingsDtoNew = new ArrayList<>();
        bookingsDtoNew.add(bookingDtoNew);
        bookingsDtoNew.add(bookingDtoNew1);

        List<BookingDto> bookings1 = bookingMapper.createDtoListBooking(bookings);
        assertEquals(bookings1, bookingsDtoNew);
    }
}