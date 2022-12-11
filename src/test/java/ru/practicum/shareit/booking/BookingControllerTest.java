package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingService bookingServiceImpl;
    private BookingDto bookingDto;
    private StartAndEndBookingDto startAndEndBookingDto;
    String jsonContent;

    private User user;

    @BeforeEach
    void beforeEach() {
        jsonContent = "{\"start\":\"2023-11-01T01:01:01\",\"end\":\"2023-12-01T01:01:01\",\"itemId\":1}";
        startAndEndBookingDto = new StartAndEndBookingDto(LocalDateTime.of(2023, 11, 1, 1, 1), LocalDateTime.of(2023, 12, 1, 1, 1), 1L);
        bookingDto = new BookingDto(1L, LocalDateTime.of(2021, 11, 1, 1, 1),
                LocalDateTime.of(2023, 12, 1, 1, 1), new BookingDto.Item(1L, "вещь"), new BookingDto.Booker(1L), BookingStatus.APPROVED);
        user = new User(2L, "Sergey1", "sergey1@gmail.com");
    }

    @Test
    void bookItemTest() throws Exception {
        when(bookingServiceImpl.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));

        verify(bookingServiceImpl, times(1))
                .createBooking(1L, startAndEndBookingDto);
    }

    @Test
    void changeOfBookingTest() throws Exception {
        when(bookingServiceImpl.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(notNullValue())));

        verify(bookingServiceImpl, times(1))
                .updateBooking(1L, 1L, true);
    }

    @Test
    void getBookingTest() throws Exception {
        when(bookingServiceImpl.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));

        verify(bookingServiceImpl, times(1))
                .getBooking(1L, 1L);
    }

    @Test
    void getBookingOfUserTest() throws Exception {
        when(bookingServiceImpl.getAllBookingsOfUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingServiceImpl, times(1))
                .getAllBookingsOfUser(1L, BookingState.ALL, 0, 10);
    }

    @Test
    void getBookingOfOwnerTest() throws Exception {
        when(bookingServiceImpl.getAllBookingsOfOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingServiceImpl, times(1))
                .getAllBookingsOfOwner(1L, BookingState.ALL, 0, 10);
    }
}
