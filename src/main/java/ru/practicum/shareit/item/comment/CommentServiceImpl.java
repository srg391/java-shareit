package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.exception.BadStatusForCommentException;
import ru.practicum.shareit.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto, long userId) {
        final Item itemInRepository = itemRepository.findById(commentDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не существует!"));
        final User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        if (!bookingService.getAllBookingsOfUser(userId, BookingState.PAST).stream()
                .anyMatch(b -> Objects.equals(b.getItem().getId(), itemInRepository.getId()))) {
            throw new BadStatusForCommentException("Вы можете оставить комментарий только после бронирования вещи!");
        }

        Comment comment = commentMapper.toComment(commentDto, author, itemInRepository);
        itemInRepository.addComment(comment);
        commentRepository.save(comment);
        return commentMapper.createDtoComment(comment);
    }
}
