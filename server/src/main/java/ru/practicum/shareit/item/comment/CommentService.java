package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.comment.dto.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, long userId);
}
