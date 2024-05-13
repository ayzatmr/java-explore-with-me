package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.enums.EventRequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime created;

    Long event;

    Long id;

    Long requester;

    EventRequestStatus status;
}
