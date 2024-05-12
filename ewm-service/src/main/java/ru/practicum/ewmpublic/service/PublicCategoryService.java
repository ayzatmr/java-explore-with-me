package ru.practicum.ewmpublic.service;


import ru.practicum.common.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> findAll(int from, int size);

    CategoryDto get(Long catId);
}
