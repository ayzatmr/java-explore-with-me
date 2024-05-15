package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.user.dto.NewCommentDto;
import ru.practicum.user.dto.UpdateCommentDto;
import ru.practicum.user.service.CommentServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentServiceImpl commentService;

    @PostMapping("/{eventId}/comments/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addCommentToEvent(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @RequestBody @Valid NewCommentDto addCommentDto) {
        return commentService.addCommentToEvent(userId, eventId, addCommentDto);
    }

    @PatchMapping("/{eventId}/comments/{userId}")
    public EventFullDto updateComment(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        return commentService.updateComment(userId, eventId, updateCommentDto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
    }
}
