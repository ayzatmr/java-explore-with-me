package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.validator.ValidIP;

import java.time.LocalDateTime;

import static ru.practicum.common.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EndpointHitDto {
    Long id;

    String app;

    String uri;

    @ValidIP
    String ip;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime timestamp;
}
