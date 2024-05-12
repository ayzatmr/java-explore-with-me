package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.common.model.Event;

import java.util.List;
import java.util.Optional;

public interface UserEventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.category " +
            "JOIN FETCH e.initiator " +
            "JOIN FETCH e.location " +
            "WHERE e.id = :eventId " +
            "AND e.initiator.id = :initiatorId")
    Optional<Event> findByEventIdAndInitiatorId(@Param("eventId") Long eventId,
                                                @Param("initiatorId") Long initiatorId);

    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.category c " +
            "JOIN FETCH e.initiator i " +
            "WHERE i.id = :initiatorId")
    List<Event> findByInitiatorId(@Param("initiatorId") Long initiatorId,
                                  Pageable pageable);
}
