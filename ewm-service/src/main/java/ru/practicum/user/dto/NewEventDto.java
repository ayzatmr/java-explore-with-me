package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.dto.LocationDto;
import ru.practicum.common.validation.ValidStartDate;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @Size(min = 20, max = 2000)
    @NotBlank
    String annotation;

    @Positive
    @NotNull
    @JsonAlias("category")
    Long categoryId;

    @Size(min = 20, max = 7000)
    @NotBlank
    String description;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    @NotNull
    @ValidStartDate(message = "Event date should be at least 2 hours after current time")
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    Boolean paid;

    @PositiveOrZero
    Long participantLimit;

    boolean requestModeration = true;

    @Size(min = 3, max = 120)
    @NotBlank
    String title;
}
