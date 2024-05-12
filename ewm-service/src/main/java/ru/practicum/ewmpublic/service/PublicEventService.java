package ru.practicum.ewmpublic.service;

import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.ewmpublic.model.EventSearchFilter;

import java.util.List;

public interface PublicEventService {
    List<EventShortDto> findEvents(EventSearchFilter searchFilter, int from, int size);

    EventFullDto getFullEventById(Long id, Long views);
}
