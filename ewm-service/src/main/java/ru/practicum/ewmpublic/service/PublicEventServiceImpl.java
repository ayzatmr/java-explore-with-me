package ru.practicum.ewmpublic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.admin.repository.AdminEventRepository;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.enums.EventSorting;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.model.Event;
import ru.practicum.common.pagination.CustomPageRequest;
import ru.practicum.ewmpublic.model.EventSearchFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.common.utils.EventSpecification.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {

    private final AdminEventRepository eventRepository;

    private final EventMapper eventMapper;

    @Override
    public List<EventShortDto> findEvents(EventSearchFilter searchFilter, int from, int size) {
        Sort sort = getSort(searchFilter.getSort());
        CustomPageRequest pageRequest = CustomPageRequest.of(from, size, sort);
        List<Specification<Event>> specifications = eventSearchFilterToSpecifications(searchFilter);
        List<Event> events = eventRepository.findAll(specifications
                        .stream()
                        .reduce(Specification::and)
                        .orElse(null),
                pageRequest).getContent();
        return eventMapper.toShortDtoList(events);
    }

    @Override
    public EventFullDto getFullEventById(Long id, Long views) {
        Event event = getEvent(id);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ObjectNotFoundException("Event is not published");
        }
        event.setViews(views);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }


    private Event getEvent(Long id) {
        return eventRepository.findFullEventById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Event is not found."));
    }

    private Sort getSort(EventSorting eventSorting) {
        Sort sort = Sort.unsorted();
        if (eventSorting == null) {
            return sort;
        }

        switch (eventSorting) {
            case VIEWS:
                sort = Sort.by(Sort.Direction.DESC, "views");
                break;
            case EVENT_DATE:
                sort = Sort.by(Sort.Direction.DESC, "eventDate");
                break;
            default:
                throw new IllegalArgumentException(eventSorting + " is not supported");
        }
        return sort;
    }

    private List<Specification<Event>> eventSearchFilterToSpecifications(EventSearchFilter searchFilter) {
        List<Specification<Event>> resultSpecification = new ArrayList<>();
        resultSpecification.add(eventStatusEquals(EventState.PUBLISHED));
        resultSpecification.add(textIn(searchFilter.getText()));
        resultSpecification.add(categoriesIdIn(searchFilter.getCategories()));
        resultSpecification.add(isPaid(searchFilter.getPaid()));
        resultSpecification.add(eventDateInRange(searchFilter.getRangeStart(), searchFilter.getRangeEnd()));
        resultSpecification.add(isAvailable(searchFilter.isOnlyAvailable()));
        return resultSpecification.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
