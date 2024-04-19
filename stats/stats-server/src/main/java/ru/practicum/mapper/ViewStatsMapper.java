package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    List<ViewStatsDto> toDtoList(List<ViewStats> viewStatsList);
}
