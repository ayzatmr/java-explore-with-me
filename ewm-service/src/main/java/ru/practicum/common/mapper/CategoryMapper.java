package ru.practicum.common.mapper;

import org.mapstruct.Mapper;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.model.Category;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> category);

    Category toModel(CategoryDto categoryDto);
}