package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    EndpointHitDto hit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
