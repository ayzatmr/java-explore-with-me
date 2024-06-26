package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;


@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    EndpointHitDto toDto(EndpointHit endpointHit);

    EndpointHit toModel(EndpointHitDto endpointHitDto);
}
