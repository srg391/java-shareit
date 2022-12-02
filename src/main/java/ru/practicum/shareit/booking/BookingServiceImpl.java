package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование c id=" + bookingId + " не существует!"));
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        if (!booking.getItem().getOwner().getId().equals(owner.getId())
                && !booking.getBooker().getId().equals(owner.getId())) {
            throw new NotFoundException("Запрос может производить только владелец или бронирующий!");
        }
        return bookingMapper.createDtoBooking(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsOfUser(long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        switch (state) {
            case ALL:
                List<Booking> bookings = bookingRepository.findByBooker_idOrderByIdDesc(user.getId());
                return bookingMapper.createDtoListBooking(bookings);
            case CURRENT:
                bookings = bookingRepository.findAllBookingsOfUserBetween(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                return bookingMapper.createDtoListBooking(bookings);
            case PAST:
                bookings = bookingRepository.findAllBookingsOfUserPast(user.getId(), LocalDateTime.now());
                return bookingMapper.createDtoListBooking(bookings);
            case FUTURE:
                bookings = bookingRepository.findAllBookingsOfUserFuture(user.getId(), LocalDateTime.now());
                return bookingMapper.createDtoListBooking(bookings);
            case WAITING:
                bookings = bookingRepository.findByBooker_idAndStatusOrderByIdDesc(user.getId(), BookingStatus.WAITING);
                return bookingMapper.createDtoListBooking(bookings);
            case REJECTED:
                bookings = bookingRepository.findByBooker_idAndStatusOrderByIdDesc(user.getId(), BookingStatus.REJECTED);
                return bookingMapper.createDtoListBooking(bookings);
            default:
                throw new BadRequestBookingException("{\"error\": \"Unknown state: UNSUPPORTED_STATUS\"}");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsOfOwner(long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        List<Item> items = itemRepository.findAll().stream()
                .filter(i -> i.getOwner() == user)
                .collect(Collectors.toList());
        List<Long> itemsIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        switch (state) {
            case ALL:
                List<Booking> bookings = bookingRepository.findAllBookingsOfItemOwner(itemsIds);
                return bookingMapper.createDtoListBooking(bookings);
            case CURRENT:
                bookings = bookingRepository.findBookingsOfItemOwnerBetween(itemsIds, LocalDateTime.now(), LocalDateTime.now());
                return bookingMapper.createDtoListBooking(bookings);
            case PAST:
                bookings = bookingRepository.findBookingsOfItemOwnerInPast(itemsIds, LocalDateTime.now());
                return bookingMapper.createDtoListBooking(bookings);
            case FUTURE:
                bookings = bookingRepository.findBookingsOfItemOwnerInFuture(itemsIds, LocalDateTime.now());
                return bookingMapper.createDtoListBooking(bookings);
            case WAITING:
                bookings = bookingRepository.findAllBookingsOfItemOwnerWithStatus(itemsIds, BookingStatus.WAITING);
                return bookingMapper.createDtoListBooking(bookings);
            case REJECTED:
                bookings = bookingRepository.findAllBookingsOfItemOwnerWithStatus(itemsIds, BookingStatus.REJECTED);
                return bookingMapper.createDtoListBooking(bookings);
            default:
                throw new BadRequestBookingException("{\"error\": \"Unknown state: UNSUPPORTED_STATUS\"}");
        }
    }

    @Override
    @Transactional
    public BookingDto createBooking(long userId, StartAndEndBookingDto startAndEndBookingDto) {
        final Item itemInRepository = itemRepository.findById(startAndEndBookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не существует!"));
        if (!itemInRepository.getAvailable()) {
            throw new ItemNotAvailableException("Вещь не доступна для бронирования!");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        if (itemInRepository.getOwner().getId().equals(user.getId())) {
            throw new BadOwnerException("Хозяин не найден!");
        }
        Booking booking = bookingMapper.createBooking(startAndEndBookingDto, itemInRepository, user);
        booking.setBooker(user);
        booking.setItem(itemInRepository);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.createDtoBooking(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto updateBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование c id=" + bookingId + " не существует!"));
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new BadStatusBookingException("Пользователь c id=" + userId + " не существует!"));
        if (!booking.getItem().getOwner().getId().equals(owner.getId())) {
            throw new BadOwnerException("Запрос может производить только владелец или бронирующий!");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BadStatusForCommentException("Статус уже APPROVED!");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updateBooking = bookingRepository.save(booking);
        return bookingMapper.createDtoBooking(updateBooking);
    }
}
