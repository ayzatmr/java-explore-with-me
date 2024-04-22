package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.common.Constants.DATE_FORMAT;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto hit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        return statService.hit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> viewStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime decodedStart = decode(start);
        LocalDateTime decodedEnd = decode(end);
        if (decodedStart.isAfter(decodedEnd)) {
            throw new ValidationException("End date should be after start date.");
        }
        return statService.viewStats(decodedStart, decodedEnd, uris, unique);
    }

    private LocalDateTime decode(String dateTime) {
        String decodedDateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodedDateTime, DATE_FORMAT);
    }

}
