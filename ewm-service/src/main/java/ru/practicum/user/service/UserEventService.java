package ru.practicum.user.service;


import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.dto.EventUpdateRequest;
import ru.practicum.user.dto.EventRequestStatusUpdateDto;
import ru.practicum.user.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.user.dto.NewEventDto;
import ru.practicum.user.dto.UserRequestDto;

import java.util.List;

public interface UserEventService {
    EventFullDto addNewEvent(Long userId, NewEventDto eventDto);

    List<EventShortDto> findAllEvents(Long userId, int from, int size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto patchEvent(Long userId, Long eventId, EventUpdateRequest eventDto);

    List<UserRequestDto> getRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateDto patchUserRequest(Long userId, Long eventId, EventRequestStatusUpdateRequestDto eventDto);

    UserRequestDto addUserRequestToEvent(Long userId, Long eventId);

    List<UserRequestDto> findUserRequests(Long userId);

    UserRequestDto cancelUserRequest(Long userId, Long requestId);
}
