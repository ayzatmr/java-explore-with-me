package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.common.Constants.DATE_FORMAT;

@WebMvcTest(controllers = StatsController.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatService statService;

    @MockBean
    private ViewStatsMapper viewStatsMapper;

    @Test
    @SneakyThrows
    void viewStats() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2024, 1, 3, 1, 1, 1);
        List<String> uris = List.of("uri");
        Boolean unique = true;

        ViewStatsDto viewStatsDto = new ViewStatsDto("appDto", "uriDto", 1L);

        when(statService.viewStats(start, end, uris, unique))
                .thenReturn(List.of(viewStatsDto));

        mvc.perform(get("/stats")
                        .param("start", start.format(DATE_FORMAT))
                        .param("end", end.format(DATE_FORMAT))
                        .params(new LinkedMultiValueMap<>(Map.of("uris", List.of("uri"))))
                        .param("unique", unique.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].app", is(viewStatsDto.getApp())))
                .andExpect(jsonPath("$.[0].uri", is(viewStatsDto.getUri())))
                .andExpect(jsonPath("$.[0].hits", is(1)));

        verify(statService, times(1)).viewStats(start, end, uris, unique);
    }


    @Test
    @SneakyThrows
    void viewStatsThrowDateTimeParseException() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 1, 1, 1);

        ViewStatsDto viewStats = new ViewStatsDto("app", "uri", 4L);

        when(statService.viewStats(start, end, null, null))
                .thenReturn(List.of(viewStats));

        mvc.perform(get("/stats")
                        .param("start", "2024-11-11")
                        .param("end", "2024-12-01"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(
                        DateTimeParseException.class, result.getResolvedException()));

        verify(statService, never()).viewStats(any(), any(), any(), any());
        verify(viewStatsMapper, never()).toDtoList(anyList());
    }

    @Test
    @SneakyThrows
    void viewStatsThrowValidationException() {
        LocalDateTime start = LocalDateTime.of(2024, 2, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 1, 1, 1);

        ViewStatsDto viewStats = new ViewStatsDto("app", "uri", 4L);

        when(statService.viewStats(start, end, null, null))
                .thenReturn(List.of(viewStats));

        mvc.perform(get("/stats")
                        .param("start", start.format(DATE_FORMAT))
                        .param("end", end.format(DATE_FORMAT)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(ValidationException.class, result.getResolvedException()));

        verify(statService, never()).viewStats(start, end, null, null);
        verify(viewStatsMapper, never()).toDtoList(anyList());
    }

    @Test
    @SneakyThrows
    void checkHitMethod() {
        EndpointHitDto endpointHitDto = new EndpointHitDto(1L, "app", "/uri/one", "1.1.1.1",
                LocalDateTime.of(2024, 12, 12, 12, 12, 12));

        when(statService.hit(endpointHitDto))
                .thenReturn(endpointHitDto);

        mvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointHitDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(endpointHitDto.getId().intValue())))
                .andExpect(jsonPath("$.app", is(endpointHitDto.getApp())))
                .andExpect(jsonPath("$.uri", is(endpointHitDto.getUri())))
                .andExpect(jsonPath("$.ip", is(endpointHitDto.getIp())))
                .andExpect(jsonPath("$.timestamp", is(endpointHitDto.getTimestamp().format(DATE_FORMAT))));

        verify(statService, times(1)).hit(endpointHitDto);
    }

    @Test
    @SneakyThrows
    void checkMethodHitWithWrongIp() {
        EndpointHitDto endpointHitDto = new EndpointHitDto(null, "app", "/uri/er", "wrongIp",
                LocalDateTime.of(2024, 2, 1, 1, 1, 1));

        mvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointHitDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()));

        verify(statService, never()).hit(any());
    }
}