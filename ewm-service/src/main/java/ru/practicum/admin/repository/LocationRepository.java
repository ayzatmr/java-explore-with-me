package ru.practicum.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.models.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
