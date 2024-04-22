package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatsRepository statsRepository;

    private final EndpointHitMapper endpointHitMapper;

    private final ViewStatsMapper viewStatsMapper;

    @Override
    public EndpointHitDto hit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.toModel(endpointHitDto);
        return endpointHitMapper.toDto(statsRepository.save(endpointHit));
    }

    @Override
    public List<ViewStatsDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return viewStatsMapper.toDtoList(getStatsFromUniqueIps(start, end, uris));
        } else {
            return viewStatsMapper.toDtoList(getAllStats(start, end, uris));
        }
    }

    private List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null) {
            return statsRepository.findStats(start, end);
        } else {
            return statsRepository.findStatsInUrlList(start, end, uris);
        }
    }

    private List<ViewStats> getStatsFromUniqueIps(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null) {
            return statsRepository.findStatsWithUniqueIps(start, end);
        } else {
            return statsRepository.findStatsInUrlListWithUniqueIp(start, end, uris);
        }
    }
}
