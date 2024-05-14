package ru.practicum.admin.service;


import ru.practicum.common.dto.CategoryDto;

public interface CategoryService {
    CategoryDto create(CategoryDto newCategoryDto);

    CategoryDto patch(Long categoryId, CategoryDto newCategoryDto);

    void delete(Long categoryId);

}
