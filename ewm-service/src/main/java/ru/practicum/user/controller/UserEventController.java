package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.dto.EventUpdateRequest;
import ru.practicum.user.dto.EventRequestStatusUpdateDto;
import ru.practicum.user.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.user.dto.NewEventDto;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.service.UserEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.model.Constants.MAX_SIZE;
import static ru.practicum.common.model.Constants.MIN_SIZE;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserEventController {
    private final UserEventService userEventService;

    @PostMapping("{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@RequestBody @Valid NewEventDto eventDto,
                               @PathVariable @Positive Long userId) {
        return userEventService.addNewEvent(userId, eventDto);
    }

    @GetMapping("{userId}/events")
    public List<EventShortDto> findAll(@PathVariable @Positive Long userId,
                                       @RequestParam(defaultValue = MIN_SIZE) @PositiveOrZero int from,
                                       @RequestParam(defaultValue = MAX_SIZE) @Positive int size) {

        return userEventService.findAllEvents(userId, from, size);
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventFullDto get(@PathVariable @Positive Long userId,
                            @PathVariable @Positive Long eventId) {

        return userEventService.getEvent(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}")
    public EventFullDto patch(@RequestBody @Valid EventUpdateRequest eventDto,
                              @PathVariable @Positive Long userId,
                              @PathVariable @Positive Long eventId) {
        return userEventService.patchEvent(userId, eventId, eventDto);
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public List<UserRequestDto> getRequests(@PathVariable @Positive Long userId,
                                            @PathVariable @Positive Long eventId) {

        return userEventService.getRequestsByUser(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateDto patch(@RequestBody @Valid EventRequestStatusUpdateRequestDto eventDto,
                                             @PathVariable @Positive Long userId,
                                             @PathVariable @Positive Long eventId) {
        return userEventService.patchUserRequest(userId, eventId, eventDto);
    }

    @GetMapping("/{userId}/requests")
    public List<UserRequestDto> findUserRequests(@PathVariable Long userId) {
        return userEventService.findUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRequestDto addUserRequestToEvent(@PathVariable Long userId,
                                                @RequestParam Long eventId) {
        return userEventService.addUserRequestToEvent(userId, eventId);
    }


    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public UserRequestDto cancelUserRequest(@PathVariable Long userId,
                                            @PathVariable Long requestId) {
        return userEventService.cancelUserRequest(userId, requestId);
    }
}
