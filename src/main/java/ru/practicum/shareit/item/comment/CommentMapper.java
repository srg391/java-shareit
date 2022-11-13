package ru.practicum.shareit.item.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    public CommentDto createDtoComment(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getDateOfCreation()
        );
    }

    public List<CommentDto> createDtoListComment(List<Comment> comments) {
        return comments.stream()
                .map(this::createDtoComment)
                .collect(Collectors.toList());
    }


    public static Comment toComment(CommentDto commentDto, User author, Item item) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                item,
                author,
                LocalDateTime.now()
        );
    }
}
