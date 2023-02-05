package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookingServiceImplTest {
    BookingService bookingServiceImpl;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingMapper bookingMapper;
    private StartAndEndBookingDto startAndEndBookingDto;
    private Booking booking;
    private Item item;
    private User user;
    private User userNotOwner;
    private Booking bookingApproved;
    private Booking bookingByNewUser;

    @BeforeEach
    void beforeEach() {
        bookingServiceImpl = new BookingServiceImpl(bookingRepository, itemRepository, userRepository, bookingMapper);
        user = new User(1L, "Sergey1", "sergey1@gmail.com");
        userNotOwner = new User(2L, "Valery2", "valery2@mail.ru");
        item = new Item(1L, "вещь", "описание вещи", true, user, 1L);
        startAndEndBookingDto = new StartAndEndBookingDto(LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), 1L);
        booking = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), item, user, BookingStatus.WAITING);
        bookingByNewUser = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), item, userNotOwner, BookingStatus.WAITING);
    }

    @Test
    void createBookingTest() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner));
        when(bookingMapper.createBooking(any(), any(), any()))
                .thenReturn(booking);
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        bookingServiceImpl.createBooking(userNotOwner.getId(), startAndEndBookingDto);

        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void createBookingByOwnerWithExceptionTest() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(1L));
        final var exception = assertThrows(RuntimeException.class, () -> bookingServiceImpl.createBooking(user.getId(), startAndEndBookingDto));

        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(0)).save(booking);
    }

    @Test
    void createBookingWithItemNotAvailableExceptionTest() {
        Item itemNew = new Item(1L, "вещь", "описание вещи", false, user, 1L);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemNew));
        final var exception = assertThrows(ItemIsNotAvailableException.class, () -> bookingServiceImpl.createBooking(user.getId(), startAndEndBookingDto));
        assertEquals("Вещь не доступна для бронирования!", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void createBookingWithItemNotFoundExceptionTest() {
        User user3 = new User();
        Item itemNew = new Item(1L, "вещь", "описание вещи", true, user3, 1L);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemNew));
        final var exception = assertThrows(NotFoundException.class, () -> bookingServiceImpl.createBooking(user.getId(), startAndEndBookingDto));
        assertEquals("Пользователь c id=1 не существует!", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void updateBookingTest() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        bookingServiceImpl.updateBooking(user.getId(), booking.getId(), false);

        verify(bookingRepository, times(1)).save(booking);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void updateBookingNotOwnerWithExceptionTest() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong()))
                .thenThrow(new NotOwnerException("Запрос может производить только владелец или бронирующий!"));

        final var exception = assertThrows(NotOwnerException.class, () -> bookingServiceImpl.updateBooking(userNotOwner.getId(), booking.getId(), false));
        assertEquals("Запрос может производить только владелец или бронирующий!", exception.getMessage());

        verify(bookingRepository, times(0)).save(booking);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void updateBookingAlreadyApprovedWithExceptionTest() {
        bookingApproved = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), item, user, BookingStatus.APPROVED);
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new BadRequestException("Статус уже APPROVED!"));
        final var exception = assertThrows(BadRequestException.class, () -> bookingServiceImpl.updateBooking(userNotOwner.getId(), bookingApproved.getId(), true));
        assertEquals("Статус уже APPROVED!", exception.getMessage());

        verify(bookingRepository, times(0)).save(booking);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void getBookingTest() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        bookingServiceImpl.getBooking(user.getId(), booking.getId());
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void getBookingWithNotFoundExceptionTest() {
        User userNotOwner1 = new User(3L, "Denis3", "denis3@mail.ru");
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner1));

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBooking(3L, 1L));
    }

    @Test
    void getAllBookingsOfUserTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner));
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findByBooker_idOrderByIdDesc(anyLong(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfUser(userNotOwner.getId(), BookingState.ALL, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(1)).findByBooker_idOrderByIdDesc(2L, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfUserBetweenTest() {
        LocalDateTime start = LocalDateTime.of(2021, 11, 2, 9, 55);
        LocalDateTime end = LocalDateTime.of(2022, 11, 9, 19, 55);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner));
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findAllBookingsOfUserBetween(anyLong(), any(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfUser(userNotOwner.getId(), BookingState.CURRENT, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(0)).findAllBookingsOfUserBetween(2L, start, end, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfUserPastTest() {
        LocalDateTime start = LocalDateTime.of(2021, 11, 2, 9, 55);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner));
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findAllBookingsOfUserPast(anyLong(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfUser(userNotOwner.getId(), BookingState.PAST, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(0)).findAllBookingsOfUserPast(2L, start, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfUserFutureTest() {
        LocalDateTime start = LocalDateTime.of(2021, 11, 2, 9, 55);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner));
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findAllBookingsOfUserFuture(anyLong(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfUser(userNotOwner.getId(), BookingState.FUTURE, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(0)).findAllBookingsOfUserFuture(2L, start, PageRequest.of(0, 10));
    }

    @Test
    void findByBooker_idAndStatusOrderByIdDescWAITINGTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner));
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findByBooker_idAndStatusOrderByIdDesc(anyLong(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfUser(userNotOwner.getId(), BookingState.WAITING, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(1)).findByBooker_idAndStatusOrderByIdDesc(2L, BookingStatus.WAITING, PageRequest.of(0, 10));
    }

    @Test
    void findByBooker_idAndStatusOrderByIdDescREJECTEDTest() {
        Booking bookingByNewUser1 = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), item, userNotOwner, BookingStatus.REJECTED);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userNotOwner));
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser1));
        when(bookingRepository.findByBooker_idAndStatusOrderByIdDesc(anyLong(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfUser(userNotOwner.getId(), BookingState.REJECTED, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(1)).findByBooker_idAndStatusOrderByIdDesc(2L, BookingStatus.REJECTED, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfOwnerTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<Item> items = new ArrayList<>(Collections.singletonList(item));
        when(itemRepository.findAll())
                .thenReturn(items);
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findAllBookingsOfItemOwner(any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfOwner(user.getId(), BookingState.ALL, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(1)).findAllBookingsOfItemOwner(List.of(1L), PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfOwnerBetweenTest() {
        LocalDateTime start = LocalDateTime.of(2021, 11, 2, 9, 55);
        LocalDateTime end = LocalDateTime.of(2022, 11, 9, 19, 55);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<Item> items = new ArrayList<>(Collections.singletonList(item));
        when(itemRepository.findAll())
                .thenReturn(items);
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findBookingsOfItemOwnerBetween(any(), any(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfOwner(user.getId(), BookingState.CURRENT, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(0)).findBookingsOfItemOwnerBetween(List.of(1L), start, end, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfOwnerPastTest() {
        LocalDateTime start = LocalDateTime.of(2021, 11, 2, 9, 55);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<Item> items = new ArrayList<>(Collections.singletonList(item));
        when(itemRepository.findAll())
                .thenReturn(items);
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findBookingsOfItemOwnerInPast(any(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfOwner(user.getId(), BookingState.PAST, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(0)).findBookingsOfItemOwnerInPast(List.of(1L), start, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfOwnerFutureTest() {
        LocalDateTime start = LocalDateTime.of(2021, 11, 2, 9, 55);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<Item> items = new ArrayList<>(Collections.singletonList(item));
        when(itemRepository.findAll())
                .thenReturn(items);
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findBookingsOfItemOwnerInFuture(any(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfOwner(user.getId(), BookingState.FUTURE, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(0)).findBookingsOfItemOwnerInFuture(List.of(1L), start, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfItemOwnerWithStatusWAITINGTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<Item> items = new ArrayList<>(Collections.singletonList(item));
        when(itemRepository.findAll())
                .thenReturn(items);
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser));
        when(bookingRepository.findAllBookingsOfItemOwnerWithStatus(any(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfOwner(user.getId(), BookingState.WAITING, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(1)).findAllBookingsOfItemOwnerWithStatus(List.of(1L), BookingStatus.WAITING, PageRequest.of(0, 10));
    }

    @Test
    void getAllBookingsOfItemOwnerWithStatusREJECTEDTest() {
        Booking bookingByNewUser1 = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2022, 11, 8, 19, 55), item, userNotOwner, BookingStatus.REJECTED);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<Item> items = new ArrayList<>(Collections.singletonList(item));
        when(itemRepository.findAll())
                .thenReturn(items);
        final List<Booking> bookings = new ArrayList<>(Collections.singletonList(bookingByNewUser1));
        when(bookingRepository.findAllBookingsOfItemOwnerWithStatus(any(), any(), PageRequest.of(anyInt(), 10)))
                .thenReturn(bookings);

        final List<BookingDto> bookingsDto = bookingServiceImpl.getAllBookingsOfOwner(user.getId(), BookingState.REJECTED, 0, 10);

        assertNotNull(bookingsDto);

        verify(bookingRepository, times(1)).findAllBookingsOfItemOwnerWithStatus(List.of(1L), BookingStatus.REJECTED, PageRequest.of(0, 10));
    }

    @Test
    void testGetBookingByIdNotUser() {
        assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBooking(3L, 1L));
    }

    @Test
    void testGetBookingByIdNotFound() {
        assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBooking(3L, 10L));
    }

}
