package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.admin.repository.AdminEventRepository;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.common.mapper.CategoryMapper;
import ru.practicum.common.model.Category;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AdminEventRepository adminEventRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto create(CategoryDto newCategoryDto) {
        Category category = categoryRepository.save(categoryMapper.toModel(newCategoryDto));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto patch(Long categoryId, CategoryDto newCategoryDto) {
        Category currentCategory = findCategory(categoryId);
        currentCategory.setName(newCategoryDto.getName());
        Category category = categoryRepository.save(currentCategory);
        return categoryMapper.toDto(category);
    }

    @Override
    public void delete(Long categoryId) {
        findCategory(categoryId);
        validateCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category is not found"));
    }

    private void validateCategory(Long catId) {
        long events = adminEventRepository.countEventsByCategoryId(catId);
        if (events > 0) {
            throw new ValidationException("Category has attached events");
        }
    }
}
