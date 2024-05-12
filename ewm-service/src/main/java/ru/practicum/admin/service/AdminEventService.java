package ru.practicum.admin.service;


import ru.practicum.admin.dto.AdminEventSearchFilter;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventUpdateRequest;

import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getFullEventByAdmin(AdminEventSearchFilter searchFilter, int from, int size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateRequest updateRequest);
}
