package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_idOrderByIdDesc(Long userId, Pageable pageable);

    List<Booking> findByBooker_idAndStartLessThanAndEndGreaterThanOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_idAndEndLessThanOrderByStartDesc(Long userId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_idAndStartGreaterThanOrderByStartDesc(Long userId, LocalDateTime start, Pageable pageable);

    List<Booking> findByBooker_idAndStatusOrderByIdDesc(Long userId, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_idInOrderByStartDesc(List<Long> itemIds, Pageable pageable);

    List<Booking> findByItem_idInAndStartLessThanAndEndGreaterThanOrderByStartDesc(List<Long> itemIds, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_idInAndEndLessThanOrderByStartDesc(List<Long> itemsIds, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_idInAndStartGreaterThanOrderByStartDesc(List<Long> itemIds, LocalDateTime start, Pageable pageable);

    List<Booking> findByItem_idInAndStatusOrderByStartDesc(List<Long> itemIds, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_owner_idAndItem_idAndEndLessThanAndStatusOrderByStartDesc(Long userId, Long itemId, LocalDateTime now, BookingStatus status);

    List<Booking> findByItem_owner_idAndItem_idAndStartGreaterThanAndStatusOrderByStartDesc(Long userId, Long itemId, LocalDateTime now, BookingStatus status);
}
