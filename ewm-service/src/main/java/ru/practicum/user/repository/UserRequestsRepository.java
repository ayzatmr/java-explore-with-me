package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.user.models.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserRequestsRepository extends JpaRepository<UserRequest, Long> {
    @Query("SELECT ur FROM UserRequest ur " +
            "JOIN FETCH ur.requester r " +
            "JOIN FETCH ur.event e " +
            "WHERE r.id = :requesterId AND e.id = :eventId")
    Optional<UserRequest> findByRequesterIdAndEventId(
            @Param("requesterId") Long requesterId,
            @Param("eventId") Long eventId);

    @Query("SELECT ur FROM UserRequest ur " +
            "JOIN FETCH ur.requester r " +
            "JOIN FETCH ur.event " +
            "e WHERE r.id = :requesterId")
    List<UserRequest> findAllByRequesterId(@Param("requesterId") Long requesterId);

    @Query("SELECT ur FROM UserRequest ur " +
            "JOIN FETCH ur.requester r " +
            "JOIN FETCH ur.event e " +
            "WHERE e.id = :eventId")
    List<UserRequest> findAllByEventId(@Param("eventId") Long eventId);

    @Query("SELECT ur FROM UserRequest ur " +
            "JOIN FETCH ur.requester " +
            "JOIN FETCH ur.event e " +
            "WHERE ur.id IN :requestIds")
    List<UserRequest> findAllByIdIn(@Param("requestIds") List<Long> requestIds);
}
