package ru.practicum.ewmpublic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.mapper.CategoryMapper;
import ru.practicum.common.model.Category;
import ru.practicum.common.model.Constants;
import ru.practicum.common.pagination.CustomPageRequest;
import ru.practicum.ewmpublic.repository.PublicCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final PublicCategoryRepository publicCategoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(int from, int size) {
        Pageable pageRequest = CustomPageRequest.of(from, size, Constants.ID_SORTING);
        List<Category> categories = publicCategoryRepository.findAll(pageRequest).toList();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDto get(Long catId) {
        Category category = publicCategoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category is not found"));
        return categoryMapper.toDto(category);
    }


}
