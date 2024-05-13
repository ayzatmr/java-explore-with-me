package ru.practicum.common.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static ru.practicum.common.model.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    final LocalDateTime timestamp = LocalDateTime.now();
    String errors;
    HttpStatus status;
    String reason;
    String message;
}
