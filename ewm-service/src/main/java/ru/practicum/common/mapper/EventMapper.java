package ru.practicum.common.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.dto.EventUpdateRequest;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.model.Category;
import ru.practicum.common.model.Event;
import ru.practicum.user.dto.NewEventDto;
import ru.practicum.user.models.Location;
import ru.practicum.user.models.NewEvent;
import ru.practicum.user.models.User;

import java.util.List;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, UserMapper.class, CategoryMapper.class, CommentMapper.class})
public interface EventMapper {

    NewEvent toModel(NewEventDto newEventDto);

    EventFullDto toDto(Event addedEvent);

    List<EventFullDto> toDtoList(List<Event> events);

    List<EventShortDto> toShortDtoList(List<Event> events);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateEvent(EventUpdateRequest updateEvent, @MappingTarget Event event);

    default Event toFullEvent(NewEvent newEventDto, Category category, User initiator, EventState state, Location location) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .title(newEventDto.getTitle())
                .eventDate(newEventDto.getEventDate())
                .category(category)
                .state(state)
                .initiator(initiator)
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .location(location)
                .build();
    }
}
