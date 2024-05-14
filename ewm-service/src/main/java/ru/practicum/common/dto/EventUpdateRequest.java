package ru.practicum.common.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.enums.StateAction;
import ru.practicum.common.validation.ValidStartDate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateRequest {

    @Size(min = 20, max = 2000)
    String annotation;

    @Positive
    @JsonAlias("category")
    Long categoryId;

    @Size(min = 20, max = 7000)
    String description;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    @ValidStartDate(message = "Event date should be at least 2 hours after current time")
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;

    @PositiveOrZero
    Long participantLimit;

    Boolean requestModeration;

    StateAction stateAction;

    @Size(min = 3, max = 120)
    String title;
}
