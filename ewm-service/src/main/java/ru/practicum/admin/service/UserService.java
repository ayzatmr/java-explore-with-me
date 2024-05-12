package ru.practicum.admin.service;


import ru.practicum.admin.dto.NewUserDto;
import ru.practicum.admin.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserDto userDto);

    List<UserDto> findAll(List<Long> ids, int size, int from);

    void delete(Long userId);

}
