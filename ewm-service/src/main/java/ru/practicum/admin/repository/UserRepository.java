package ru.practicum.admin.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    default List<User> findByIdIn(List<Long> ids, Pageable pageable) {
        if (ids == null) {
            return findAll(pageable).getContent();
        } else {
            return findByIdIn(ids, pageable);
        }
    }
}
