package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {

    private final String httpHeaderUserId = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(httpHeaderUserId) long userId,
                                 @PathVariable("bookingId") long bookingId) {
        BookingDto bookingDto = bookingService.getBooking(userId, bookingId);
        log.debug("Бронирование с id :" + bookingId);
        return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getBookingOfUser(@RequestHeader(httpHeaderUserId) long userId,
                                             @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                             @RequestParam(name = "from", defaultValue = "0") int from,
                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Состояние не известно : " + stateParam);
        }
        List<BookingDto> bookings = bookingService.getAllBookingsOfUser(userId, state, from, size);
        log.debug("Количество бронирований : " + bookings.size());
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingOfOwner(@RequestHeader(httpHeaderUserId) long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @RequestParam(name = "from", defaultValue = "0") int from,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Состояние не известно: " + stateParam);
        }
        List<BookingDto> bookings = bookingService.getAllBookingsOfOwner(userId, state, from, size);
        log.info("Количество бронирований пользователя : " + bookings.size());
        return bookings;
    }

    @PostMapping
    public BookingDto bookingItem(@RequestHeader(httpHeaderUserId) long userId,
                                  @RequestBody StartAndEndBookingDto startAndEndBookingDto) {
        BookingDto bookingDtoCreated = bookingService.createBooking(userId, startAndEndBookingDto);
        log.debug("Создано бронирование у пользователя с id :" + userId);
        return bookingDtoCreated;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeOfBooking(@RequestHeader(httpHeaderUserId) long userId,
                                      @PathVariable("bookingId") long bookingId,
                                      @RequestParam("approved") Boolean approved) {
        BookingDto bookingDto = bookingService.updateBooking(userId, bookingId, approved);
        log.debug("Создана бронирование c id :" +  bookingId + "у пользователя с id :" + userId);
        return bookingDto;
    }

}
