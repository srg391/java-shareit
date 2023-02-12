package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {
    private CommentMapper commentMapper;
    private ItemMapper itemMapper;

    private User user1;

    private Item item1;

    private Item item2;


    @BeforeEach
    void beforeEach() {
        commentMapper = new CommentMapper();
        itemMapper = new ItemMapper(commentMapper);
        user1 = new User(1L, "Sergey1", "sergey1@gmail.com");
        item1 = new Item(1L, "вещь", "описание вещи", true, user1, 1L);
        item2 = new Item(2L, "другая вещь", "описание другой вещи", true, user1, 2L);
    }

    @Test
    void createItemTest() {
        ItemDto itemDto1 = new ItemDto(1L, "вещь", "описание вещи", true, 1L);
        ItemDto itemDto2 = new ItemDto(2L, "другая вещь", "описание другой вещи", true, 2L);

        Item itemThing1 = itemMapper.createItem(itemDto1, user1);
        Item itemThing2 = itemMapper.createItem(itemDto2, user1);

        assertEquals(itemThing1, item1);
        assertEquals(itemThing2, item2);
    }

    @Test
    void createDtoItemTest() {
        ItemDto itemDto1 = new ItemDto(1L, "вещь", "описание вещи", true, 1L);
        ItemDto itemDto2 = new ItemDto(2L, "другая вещь", "описание другой вещи", true, 2L);

        ItemDto itemDtoThing1 = itemMapper.createDtoItem(item1);
        ItemDto itemDtoThing2 = itemMapper.createDtoItem(item2);

        assertEquals(itemDtoThing1, itemDto1);
        assertEquals(itemDtoThing2, itemDto2);
    }

    @Test
    void createDtoListUserTest() {
        ItemDto itemDto1 = new ItemDto(1L, "вещь", "описание вещи", true, 1L);
        ItemDto itemDto2 = new ItemDto(2L, "другая вещь", "описание другой вещи", true, 2L);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        List<ItemDto> itemsDtoNew = new ArrayList<>();
        itemsDtoNew.add(itemDto1);
        itemsDtoNew.add(itemDto2);

        List<ItemDto> itemsDto = itemMapper.createDtoListItem(items);

        assertEquals(itemsDto, itemsDtoNew);
    }

    @Test
    void createDtoItemWithBookingTest() {
        User user2 = new User(2L, "Valery2", "valery2@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2021, 11, 3, 10, 55), item1, user2, BookingStatus.CANCELED);
        Booking booking2 = new Booking(1L, LocalDateTime.of(2021, 11, 3, 11, 55), LocalDateTime.of(2021, 11, 3, 12, 55), item1, user2, BookingStatus.CANCELED);
        ItemWithBookingDto.Booking bookingDto1 = new ItemWithBookingDto.Booking(1L, 1L);
        ItemWithBookingDto.Booking bookingDto2 = new ItemWithBookingDto.Booking(2L, 2L);
        Comment comment = new Comment(1L, "коментарий к вещи", item1, user1, null);
        item1.addComment(comment);
        CommentDto commentDto = new CommentDto(1L, "коментарий к вещи", 1L, "Sergey1", null);
        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "вещь", "описание вещи", true, 1L, comments, bookingDto1, bookingDto2);
        ItemWithBookingDto itemWithBookingDtoNew = itemMapper.createDtoItemWithBooking(item1, booking1, booking2);
        assertEquals(itemWithBookingDtoNew, itemWithBookingDto);
    }

    @Test
    void createDtoItemWithBookingWithBookingsNullTest() {
        User user2 = new User(2L, "Valery2", "valery2@mail.ru");
        Booking booking1 = null;
        Booking booking2 = null;
        ItemWithBookingDto.Booking bookingDto1 = null;
        ItemWithBookingDto.Booking bookingDto2 = null;
        Comment comment = new Comment(1L, "коментарий к вещи", item1, user1, null);
        item1.addComment(comment);
        CommentDto commentDto = new CommentDto(1L, "коментарий к вещи", 1L, "Sergey1", null);
        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "вещь", "описание вещи", true, 1L, comments, bookingDto1, bookingDto2);
        ItemWithBookingDto itemWithBookingDtoNew = itemMapper.createDtoItemWithBooking(item1, booking1, booking2);
        assertEquals(itemWithBookingDtoNew, itemWithBookingDto);
    }

    @Test
    void createDtoItemWithBookingWithOneBookingNullTest() {
        User user2 = new User(2L, "Valery2", "valery2@mail.ru");
        Booking booking1 = null;
        Booking booking2 = new Booking(1L, LocalDateTime.of(2021, 11, 3, 11, 55), LocalDateTime.of(2021, 11, 3, 12, 55), item1, user2, BookingStatus.CANCELED);
        ItemWithBookingDto.Booking bookingDto1 = null;
        ItemWithBookingDto.Booking bookingDto2 = new ItemWithBookingDto.Booking(2L, 2L);
        Comment comment = new Comment(1L, "коментарий к вещи", item1, user1, null);
        item1.addComment(comment);
        CommentDto commentDto = new CommentDto(1L, "коментарий к вещи", 1L, "Sergey1", null);
        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "вещь", "описание вещи", true, 1L, comments, bookingDto1, bookingDto2);
        ItemWithBookingDto itemWithBookingDtoNew = itemMapper.createDtoItemWithBooking(item1, booking1, booking2);
        assertEquals(itemWithBookingDtoNew, itemWithBookingDto);
    }

    @Test
    void createDtoItemWithBookingWithTwoBookingNullTest() {
        User user2 = new User(2L, "Valery2", "valery2@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.of(2021, 11, 3, 9, 55), LocalDateTime.of(2021, 11, 3, 10, 55), item1, user2, BookingStatus.CANCELED);
        Booking booking2 = null;
        ItemWithBookingDto.Booking bookingDto1 = new ItemWithBookingDto.Booking(1L, 1L);
        ItemWithBookingDto.Booking bookingDto2 = null;
        Comment comment = new Comment(1L, "коментарий к вещи", item1, user1, null);
        item1.addComment(comment);
        CommentDto commentDto = new CommentDto(1L, "коментарий к вещи", 1L, "Sergey1", null);
        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "вещь", "описание вещи", true, 1L, comments, bookingDto1, bookingDto2);
        ItemWithBookingDto itemWithBookingDtoNew = itemMapper.createDtoItemWithBooking(item1, booking1, booking2);
        assertEquals(itemWithBookingDtoNew, itemWithBookingDto);
    }
}
