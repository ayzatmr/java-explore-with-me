package ru.practicum.user.service;

import ru.practicum.common.dto.EventFullDto;
import ru.practicum.user.dto.NewCommentDto;
import ru.practicum.user.dto.UpdateCommentDto;

public interface CommentService {

    EventFullDto addCommentToEvent(Long userId, Long eventId, NewCommentDto commentDto);

    EventFullDto updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto);

    void deleteComment(Long userId, Long commentId);
}
