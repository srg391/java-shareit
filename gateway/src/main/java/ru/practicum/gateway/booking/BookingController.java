package ru.practicum.gateway.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.booking.dto.BookingItemRequestDto;
import ru.practicum.gateway.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final String httpHeaderUserId = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(httpHeaderUserId) long userId,
                                             @PathVariable Long bookingId) {
        ResponseEntity<Object> booking = bookingClient.getBooking(userId, bookingId);
        log.debug("Бронирования с id = " + bookingId + " Пользвателя с id = " + userId);
        return booking;
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(httpHeaderUserId) long userId,
                                     @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестное состояние бронирования: " + stateParam));
        ResponseEntity<Object> bookings = bookingClient.getBookings(userId, state, from, size);
        log.debug("Бронирования с состоянием : " + stateParam + ", Пользователя с id : " + userId);
        return bookings;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingOfOwner(@RequestHeader(httpHeaderUserId) long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестное состояние бронирования: " + stateParam));
        ResponseEntity<Object> bookings = bookingClient.getBookingOfOwner(userId, state, from, size);
        log.info("Бронирований пользователя с id = " + userId);
        return bookings;
    }

    @PostMapping
    public ResponseEntity<Object> bookingItem(@RequestHeader(httpHeaderUserId) long userId,
                                              @Valid @RequestBody BookingItemRequestDto bookingItemRequestDto) {
        ResponseEntity<Object> bookingCreated = bookingClient.bookingItem(userId, bookingItemRequestDto);
        log.debug("Создано бронирование у пользователя с id :" + userId);
        return bookingCreated;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeOfBooking(@RequestHeader(httpHeaderUserId) long userId,
                                      @PathVariable("bookingId") long bookingId,
                                      @RequestParam("approved") Boolean approved) {
        ResponseEntity<Object> booking = bookingClient.updateBooking(userId, bookingId, approved);
        log.debug("Создана бронирование c id :" +  bookingId + "у пользователя с id :" + userId);
        return booking;
    }
}
