package ru.practicum.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    Long id;

    String annotation;

    CategoryDto category;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime eventDate;

    UserShortDto initiator;

    Boolean paid;

    String title;

    long confirmedRequests;

    long views;
}
