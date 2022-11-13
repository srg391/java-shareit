package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable("bookingId") long bookingId) {
    BookingDto bookingDto = bookingService.getBooking(userId, bookingId);
    log.debug("Бронирование с id :" + bookingId);
    return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getBookingOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        List<BookingDto> bookings = bookingService.getAllBookingsOfUser(userId, state);
        log.debug("Количество бронирований : " + bookings.size());
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingOfOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        List<BookingDto> bookings = bookingService.getAllBookingsOfOwner(userId, state);
        log.info("Количество бронирований пользователя : " + bookings.size());
        return bookings;
    }

    @PostMapping
    public BookingDto bookingItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @Valid @RequestBody StartAndEndBookingDto startAndEndBookingDto) {
        BookingDto bookingDtoCreated = bookingService.createBooking(userId, startAndEndBookingDto);
        log.debug("Создано бронирование у пользователя с id :" + userId);
        return bookingDtoCreated;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeOfBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable("bookingId") long bookingId,
                                      @RequestParam("approved") Boolean approved) {
        BookingDto bookingDto = bookingService.updateBooking(userId, bookingId, approved);
        log.debug("Создана бронирование c id :" +  bookingId + "у пользователя с id :" + userId);
        return bookingDto;
    }

}
