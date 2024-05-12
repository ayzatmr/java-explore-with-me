package ru.practicum.ewmpublic.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.common.model.Category;

public interface PublicCategoryRepository extends JpaRepository<Category, Long> {
}
