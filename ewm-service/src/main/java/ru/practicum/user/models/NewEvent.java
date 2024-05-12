package ru.practicum.user.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEvent {

    String annotation;

    Long categoryId;

    String description;

    LocalDateTime eventDate;

    String title;

    Location location;

    boolean paid;

    int participantLimit;

    boolean requestModeration;
}
