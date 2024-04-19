package ru.practicum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class StatRepositoryTest {

    private EndpointHit savedEndpointHit1;
    private EndpointHit savedEndpointHit2;

    @Autowired
    private StatsRepository statsRepository;

    @BeforeEach
    void init() {
        savedEndpointHit1 = statsRepository.save(new EndpointHit(null, "app", "/app", "1.1.1.1",
                LocalDateTime.of(2024, 1, 1, 1, 1, 1)));
        savedEndpointHit2 = statsRepository.save(new EndpointHit(null, "app2", "/app2", "1.1.1.2",
                LocalDateTime.of(2024, 2, 1, 1, 1, 1)));
    }

    @Test
    void findStats() {
        List<ViewStats> stats = statsRepository.findStats(
                LocalDateTime.of(2024, 1, 1, 1, 1, 1),
                LocalDateTime.of(2024, 2, 2, 2, 2, 2));
        assertThat(stats.size(), is(2));
        assertThat(stats.get(0).getUri(), is(savedEndpointHit1.getUri()));
        assertThat(stats.get(0).getHits(), is(1L));
        assertThat(stats.get(1).getUri(), is(savedEndpointHit2.getUri()));
        assertThat(stats.get(1).getHits(), is(1L));
    }

    @Test
    void findStatsInUrlList() {
        List<ViewStats> stats = statsRepository.findStatsInUrlList(
                LocalDateTime.of(2024, 1, 1, 1, 1, 1),
                LocalDateTime.of(2025, 2, 1, 1, 1, 2),
                List.of("/app", "/app2"));
        assertThat(stats.size(), is(2));
        assertThat(stats.get(0).getUri(), is(savedEndpointHit1.getUri()));
        assertThat(stats.get(0).getHits(), is(1L));
        assertThat(stats.get(1).getUri(), is(savedEndpointHit2.getUri()));
        assertThat(stats.get(1).getHits(), is(1L));
    }

    @Test
    void findStatsInUrlListWithUniqueIp() {
        statsRepository.save(new EndpointHit(null, "app2", "/app2", "1.1.1.2",
                LocalDateTime.of(2024, 2, 1, 1, 1, 1)));
        List<ViewStats> stats = statsRepository.findStatsInUrlListWithUniqueIp(
                LocalDateTime.of(2024, 1, 1, 1, 1, 1),
                LocalDateTime.of(2024, 2, 2, 2, 2, 2),
                List.of("/app1", "/app2"));
        assertThat(stats.size(), is(1));
        assertThat(stats.get(0).getUri(), is(savedEndpointHit2.getUri()));
        assertThat(stats.get(0).getHits(), is(1L));
    }

    @Test
    void findStatsWithUniqueIps() {
        statsRepository.save(new EndpointHit(null, "app2", "/app2", "1.1.1.2",
                LocalDateTime.of(2024, 2, 1, 1, 1, 1)));
        List<ViewStats> stats = statsRepository.findStatsWithUniqueIps(
                LocalDateTime.of(2024, 1, 1, 1, 1, 1),
                LocalDateTime.of(2024, 2, 2, 2, 2, 2));
        assertThat(stats.size(), is(2));
        assertThat(stats.get(0).getUri(), is(savedEndpointHit2.getUri()));
        assertThat(stats.get(0).getHits(), is(1L));
    }
}