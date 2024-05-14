package ru.practicum.admin.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.common.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEventSearchFilter {

    List<Long> users;

    List<EventState> states;

    List<Long> categories;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime rangeStart;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime rangeEnd;

    boolean onlyAvailable;
}
