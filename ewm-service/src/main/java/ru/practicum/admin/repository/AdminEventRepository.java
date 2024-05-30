package ru.practicum.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.common.model.Event;

import java.util.Optional;

public interface AdminEventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.category c " +
            "JOIN FETCH e.initiator i " +
            "JOIN FETCH  e.location " +
            "LEFT JOIN FETCH e.comments " +
            "WHERE e.id = ?1")
    Optional<Event> findFullEventById(Long eventId);

    long countEventsByCategoryId(Long categoryId);
}
