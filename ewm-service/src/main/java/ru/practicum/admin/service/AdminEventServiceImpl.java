package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.dto.AdminEventSearchFilter;
import ru.practicum.admin.repository.AdminEventRepository;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventUpdateRequest;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.enums.StateAction;
import ru.practicum.common.exception.AlreadyExistException;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.model.Constants;
import ru.practicum.common.model.Event;
import ru.practicum.common.pagination.CustomPageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.common.utils.EventSpecification.*;


@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final AdminEventRepository adminEventRepository;

    private final EventMapper eventMapper;

    @Override
    public List<EventFullDto> getFullEventByAdmin(AdminEventSearchFilter searchFilter, int from, int size) {
        CustomPageRequest pageRequest = CustomPageRequest.of(from, size, Constants.ID_SORTING);
        List<Specification<Event>> specifications = eventAdminSearchFilterToSpecifications(searchFilter);
        List<Event> events = adminEventRepository.findAll(specifications
                        .stream()
                        .reduce(Specification::and)
                        .orElse(null),
                pageRequest).getContent();
        return eventMapper.toDtoList(events);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, EventUpdateRequest updateRequest) {
        Event event = getEvent(eventId);
        eventMapper.updateEvent(updateRequest, event);
        updateEventState(updateRequest.getStateAction(), event);
        Event savedEvent = adminEventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    private void updateEventState(StateAction stateAction, Event event) {
        if (stateAction == null) {
            return;
        }
        switch (stateAction) {
            case PUBLISH_EVENT:
                validateCancelled(event);
                validatePublished(event);
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case REJECT_EVENT:
                validatePublished(event);
                event.setState(EventState.CANCELED);
                break;
        }
    }

    private void validateCancelled(Event event) {
        if (event.getState().equals(EventState.CANCELED)) {
            throw new AlreadyExistException("Can not publish cancelled event");
        }
    }

    private void validatePublished(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new AlreadyExistException("Event is already published");
        }
    }

    private Event getEvent(Long id) {
        return adminEventRepository.findFullEventById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Event is not found."));
    }

    private List<Specification<Event>> eventAdminSearchFilterToSpecifications(AdminEventSearchFilter searchFilter) {
        List<Specification<Event>> resultSpecification = new ArrayList<>();
        resultSpecification.add(eventStatusIn(searchFilter.getStates()));
        resultSpecification.add(initiatorIdIn(searchFilter.getUsers()));
        resultSpecification.add(categoriesIdIn(searchFilter.getCategories()));
        resultSpecification.add(eventDateInRange(searchFilter.getRangeStart(), searchFilter.getRangeEnd()));
        resultSpecification.add(isAvailable(searchFilter.isOnlyAvailable()));
        return resultSpecification.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
