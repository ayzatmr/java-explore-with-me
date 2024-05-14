package ru.practicum.common.mapper;

import org.mapstruct.Mapper;
import ru.practicum.common.dto.LocationDto;
import ru.practicum.user.models.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toDto(Location location);
}
