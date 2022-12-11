package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.StartAndEndBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private StartAndEndBookingDto startAndEndBookingDto;
    private Booking booking;
    private Item item;
    private User userOwner;
    private User userNotOwner;
    private Booking bookingByNewUser;


    @BeforeEach
    void beforeEach() {
        userOwner = new User(null, "Sergey1", "sergey1@gmail.com");
        userNotOwner = new User(null, "Valery2", "valery2@mail.ru");
        item = new Item(null, "вещь", "описание вещи", true, userOwner, null);
        startAndEndBookingDto = new StartAndEndBookingDto(LocalDateTime.of(2021, 12, 1, 1, 1), LocalDateTime.of(2022, 12, 1, 1, 1), 1L);
        booking = new Booking(null, LocalDateTime.of(2021, 12, 1, 1, 1), LocalDateTime.of(2022, 12, 2, 1, 1), item, userOwner, WAITING);
        bookingByNewUser = new Booking(null, LocalDateTime.of(2023, 12, 1, 1, 1), LocalDateTime.of(2023, 12, 5, 1, 1), item, userNotOwner, WAITING);
    }

    @Test
    void findByBooker_idOrderByIdDescTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findByBooker_idOrderByIdDesc(userNotOwner.getId(), PageRequest.of(0, 10));

        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findAllBookingsOfUserBetweenTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findAllBookingsOfUserBetween(userNotOwner.getId(), LocalDateTime.of(2022, 12, 1, 1, 1), LocalDateTime.now(), PageRequest.of(0, 10));

        then(bookingsList).size().isEqualTo(0);
    }

    @Test
    void findAllBookingsOfUserPastTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findAllBookingsOfUserPast(userNotOwner.getId(), LocalDateTime.of(2023, 12, 6, 1, 1), PageRequest.of(0, 10));

        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findAllBookingsOfUserFutureTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findAllBookingsOfUserFuture(userNotOwner.getId(), LocalDateTime.of(2022, 11, 1, 1, 1), PageRequest.of(0, 10));

        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findByBooker_idAndStatusOrderByIdDescTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsListWait = bookingRepository.findByBooker_idAndStatusOrderByIdDesc(userNotOwner.getId(), WAITING, PageRequest.of(0, 10));
        List<Booking> bookingsListApp = bookingRepository.findByBooker_idAndStatusOrderByIdDesc(userNotOwner.getId(), APPROVED, PageRequest.of(0, 10));
        then(bookingsListWait).size().isEqualTo(1);
        then(bookingsListApp).size().isEqualTo(0);
    }

    @Test
    void findAllBookingsOfItemOwnerTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findAllBookingsOfItemOwner(List.of(item.getId()), PageRequest.of(0, 10));
        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findBookingsOfItemOwnerBetweenTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findBookingsOfItemOwnerBetween(List.of(item.getId()), LocalDateTime.of(2023, 12, 2, 1, 1), LocalDateTime.of(2023, 12, 3, 1, 1), PageRequest.of(0, 10));
        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findBookingsOfItemOwnerInPastTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findBookingsOfItemOwnerInPast(List.of(item.getId()), LocalDateTime.of(2023, 12, 8, 19, 55), PageRequest.of(0, 10));
        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findBookingsOfItemOwnerInFutureTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findBookingsOfItemOwnerInFuture(List.of(item.getId()), LocalDateTime.now(), PageRequest.of(0, 10));
        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findAllBookingsOfItemOwnerWithStatusTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        testEntityManager.persist(bookingByNewUser);

        List<Booking> bookingsList = bookingRepository.findAllBookingsOfItemOwnerWithStatus(List.of(item.getId()), WAITING, PageRequest.of(0, 10));
        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findLastBookingsTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        booking = new Booking(null, LocalDateTime.of(2021, 12, 1, 1, 1), LocalDateTime.of(2021, 12, 2, 1, 1), item, userNotOwner, WAITING);
        testEntityManager.persist(booking);

        List<Booking> bookingsList = bookingRepository.findLastBookings(userOwner.getId(), item.getId(), LocalDateTime.now(), WAITING);
        then(bookingsList).size().isEqualTo(1);
    }

    @Test
    void findFutureBookingsTest() {
        testEntityManager.persist(userOwner);
        testEntityManager.persist(userNotOwner);
        testEntityManager.persist(item);
        booking = new Booking(null, LocalDateTime.of(2023, 12, 1, 1, 1), LocalDateTime.of(2023, 12, 2, 1, 1), item, userNotOwner, WAITING);
        testEntityManager.persist(booking);

        List<Booking> bookingsList = bookingRepository.findFutureBookings(userOwner.getId(), item.getId(), LocalDateTime.now(), WAITING);
        then(bookingsList).size().isEqualTo(1);
    }
}
