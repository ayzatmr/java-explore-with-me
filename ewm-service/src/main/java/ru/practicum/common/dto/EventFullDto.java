package ru.practicum.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.enums.EventState;

import java.time.LocalDateTime;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;

    String annotation;

    CategoryDto category;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime eventDate;

    UserShortDto initiator;

    boolean paid;

    String description;

    String title;

    int confirmedRequests;

    long views;

    int participantLimit;

    boolean requestModeration;

    EventState state;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime createdOn;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime publishedOn;

    LocationDto location;

}
