package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.admin.repository.AdminEventRepository;
import ru.practicum.admin.repository.UserRepository;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.exception.AlreadyExistException;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.mapper.CommentMapper;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.model.Comment;
import ru.practicum.common.model.Event;
import ru.practicum.user.dto.NewCommentDto;
import ru.practicum.user.dto.UpdateCommentDto;
import ru.practicum.user.models.User;
import ru.practicum.user.repository.CommentRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final AdminEventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public EventFullDto addCommentToEvent(Long userId, Long eventId, NewCommentDto commentDto) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        Comment comment = commentMapper.toModel(commentDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        Comment savedComment = commentRepository.save(comment);
        event.addCommentToEvent(savedComment);
        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateComment(Long userId, Long eventId, UpdateCommentDto updateComment) {
        getUser(userId);
        Comment comment = getComment(updateComment.getCommentId());
        validateAuthor(userId, comment);
        comment.setText(updateComment.getText());
        commentRepository.save(comment);
        return eventMapper.toDto(getEvent(eventId));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        getUser(userId);
        Comment comment = getComment(commentId);
        validateAuthor(userId, comment);
        commentRepository.deleteById(commentId);
    }

    private void validateAuthor(Long userId, Comment comment) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new AlreadyExistException("User is not an author of the comment");
        }
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findCommentById(commentId)
                .orElseThrow(() -> new ObjectNotFoundException("Comment is not found"));
    }

    private Event getEvent(Long id) {
        return eventRepository.findFullEventById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Event is not found"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User is not found."));
    }
}
