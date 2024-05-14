package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.AdminEventSearchFilter;
import ru.practicum.admin.service.AdminEventService;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.model.Constants.MAX_SIZE;
import static ru.practicum.common.model.Constants.MIN_SIZE;


@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> getFullEventsInfoByAdmin(
            AdminEventSearchFilter searchFilter,
            @RequestParam(defaultValue = MIN_SIZE) @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size) {
        return adminEventService.getFullEventByAdmin(searchFilter, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid EventUpdateRequest updateRequest) {
        return adminEventService.updateEventByAdmin(eventId, updateRequest);
    }
}
