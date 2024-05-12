package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatClient {

    private final WebClient webClient;

    public EndpointHitDto hitDto(EndpointHitDto endpointHitDto) {
        String uri = "/hits";
        return webClient
                .post()
                .uri(uri)
                .bodyValue(endpointHitDto)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String requestUrl = String.format("/stats?start=%s&end=%s&uris=%s&unique=%s",
                URLEncoder.encode(String.valueOf(start), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(end), StandardCharsets.UTF_8),
                uris,
                unique);

        return webClient.get()
                .uri(requestUrl)
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .collectList()
                .block();
    }

    public ViewStatsDto getUniqueIpStatsForUri(String uri) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/statistic")
                        .queryParam("uri", uri)
                        .build())
                .retrieve()
                .bodyToMono(ViewStatsDto.class)
                .block();
    }
}
