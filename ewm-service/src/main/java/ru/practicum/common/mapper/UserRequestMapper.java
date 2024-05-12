package ru.practicum.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.models.UserRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    UserRequestDto toDto(UserRequest userRequest);

    List<UserRequestDto> toDtoList(List<UserRequest> userRequests);
}
