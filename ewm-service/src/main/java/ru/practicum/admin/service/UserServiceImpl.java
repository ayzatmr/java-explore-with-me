package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.NewUserDto;
import ru.practicum.admin.dto.UserDto;
import ru.practicum.admin.repository.UserRepository;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.mapper.UserMapper;
import ru.practicum.common.model.Constants;
import ru.practicum.common.pagination.CustomPageRequest;
import ru.practicum.user.models.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(NewUserDto userDto) {
        User user = userRepository.save(userMapper.toModel(userDto));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll(List<Long> ids, int from, int size) {
        Pageable pageRequest = CustomPageRequest.of(from, size, Constants.ID_SORTING);
        List<User> users = userRepository.findByIdIn(ids, pageRequest);
        return userMapper.toDtoList(users);
    }

    @Override
    public void delete(Long userId) {
        findUser(userId);
        userRepository.deleteById(userId);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User is not found"));
    }
}
