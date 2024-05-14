package ru.practicum.ewmpublic.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.common.enums.EventSorting;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventSearchFilter {

    String text;

    List<Long> categories;

    Boolean paid;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime rangeStart;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime rangeEnd;

    boolean onlyAvailable;

    EventSorting sort;
}
