package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentServiceImplTest {
    private CommentServiceImpl commentServiceImpl;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingServiceImpl bookingServiceImpl;
    @Mock
    private CommentMapper commentMapper;

    private Comment comment;
    private User user1;
    private Item item;
    private BookingDto bookingDto;
    private CommentDto commentDto;


    @BeforeEach
    void beforeEach() {
        commentServiceImpl = new CommentServiceImpl(commentRepository, itemRepository, userRepository, bookingServiceImpl, commentMapper);
        user1 = new User(1L, "Sergey1", "sergey1@gmail.com");
        item = new Item(1L, "вещь", "описание вещи", true, user1, 1L);
        comment = new Comment(1L, "коментарий о вещи", item, user1, null);
        bookingDto = new BookingDto(1L, LocalDateTime.of(2022, 10, 1, 11, 20), LocalDateTime.of(2022, 10, 4, 11, 20), new BookingDto.Item(1L, "вещь"), new BookingDto.Booker(1L), BookingStatus.APPROVED);
        commentDto = new CommentDto(1L, "коментарий о вещи", 1L, "Sergey1", null);
    }

    @Test
    void createCommentTest() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));
        when(bookingServiceImpl.getAllBookingsOfUser(user1.getId(), BookingState.PAST, 0, 10))
                .thenReturn(List.of(bookingDto));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        commentServiceImpl.createComment(commentDto, 1L);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void createCommentWithNotFoundItemExTest() {
        when(itemRepository.findById(1L));
        final var exception = assertThrows(RuntimeException.class, () -> commentServiceImpl.createComment(commentDto, 1L));
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void createCommentWithBadStatusForCommentExceptionTest() {
        Item itemNew = new Item(1L, "вещь", "описание вещи", false, user1, 1L);
        User user2 = new User(2L, "Valery2", "valery2@mail.ru");
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemNew));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user2));
        final var exception = assertThrows(RuntimeException.class, () -> commentServiceImpl.createComment(commentDto, 1L));
        assertEquals("Вы можете оставить комментарий только после бронирования вещи!", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
    }
}
