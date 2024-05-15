package ru.practicum.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.common.model.Comment;
import ru.practicum.user.dto.CommentDto;
import ru.practicum.user.dto.NewCommentDto;
import ru.practicum.user.dto.UpdateCommentDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {

    CommentDto toDto(Comment comment);

    Comment toModel(NewCommentDto newCommentDto);

    @Mapping(source = "commentId", target = "id")
    Comment toModel(UpdateCommentDto updateCommentDto);

    List<NewCommentDto> toDtoList(List<Comment> comments);
}
