package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.NewUserDto;
import ru.practicum.admin.dto.UserDto;
import ru.practicum.admin.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.model.Constants.MAX_SIZE;
import static ru.practicum.common.model.Constants.MIN_SIZE;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid NewUserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                                 @RequestParam(defaultValue = MIN_SIZE) @PositiveOrZero int from,
                                 @RequestParam(defaultValue = MAX_SIZE) @Positive int size) {
        return userService.findAll(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable Long userId) {
        userService.delete(userId);
    }
}
