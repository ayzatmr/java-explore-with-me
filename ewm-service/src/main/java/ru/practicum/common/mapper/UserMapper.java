package ru.practicum.common.mapper;

import org.mapstruct.Mapper;
import ru.practicum.admin.dto.NewUserDto;
import ru.practicum.admin.dto.UserDto;
import ru.practicum.user.models.User;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(NewUserDto newUserDto);

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> userList);
}