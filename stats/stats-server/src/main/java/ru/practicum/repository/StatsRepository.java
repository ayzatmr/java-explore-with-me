package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value = "SELECT NEW ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT NEW ru.practicum.model.ViewStats(h.app, h.uri, COUNT(distinct (h.ip))) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findStatsWithUniqueIps(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT NEW ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri in (?3)" +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findStatsInUrlList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT NEW ru.practicum.model.ViewStats(h.app, h.uri, COUNT(distinct(h.ip))) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri in (?3)" +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findStatsInUrlListWithUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, COUNT(DISTINCT(eh.ip))) " +
            "FROM EndpointHit eh " +
            "WHERE eh.uri = ?1 " +
            "GROUP BY eh.app, eh.uri")
    ViewStats getUniqueIpStatsByUri(String uri);
}
