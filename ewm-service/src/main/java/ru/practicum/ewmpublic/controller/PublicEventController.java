package ru.practicum.ewmpublic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatClient;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.ewmpublic.model.EventSearchFilter;
import ru.practicum.ewmpublic.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.common.model.Constants.MAX_SIZE;
import static ru.practicum.common.model.Constants.MIN_SIZE;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {

    private static final String SERVICE_ID = "ewm-service";

    private final PublicEventService eventService;

    private final StatClient statClient;

    @GetMapping
    public List<EventShortDto> findAll(
            EventSearchFilter searchFilter,
            @RequestParam(defaultValue = MIN_SIZE) @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size,
            HttpServletRequest request) {
        validateDate(searchFilter);
        List<EventShortDto> events = eventService.findEvents(searchFilter, from, size);
        sendStatistics(request);
        return events;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(
            @PathVariable @Positive Long id,
            HttpServletRequest request) {
        sendStatistics(request);
        Long hits = statClient.getUniqueIpStatsForUri(request.getRequestURI()).getHits();
        return eventService.getFullEventById(id, hits);
    }

    private void sendStatistics(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(SERVICE_ID)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        statClient.hitDto(endpointHitDto);
    }

    private void validateDate(EventSearchFilter searchFilter) {
        if (searchFilter.getRangeStart() != null && searchFilter.getRangeEnd() != null) {
            if (searchFilter.getRangeStart().isAfter(searchFilter.getRangeEnd())) {
                throw new ValidationException("Wrong date range");
            }
        }
    }
}
