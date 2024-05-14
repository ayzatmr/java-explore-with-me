package ru.practicum.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.enums.EventRequestStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequestDto {
    List<Long> requestIds;

    EventRequestStatus status;
}
