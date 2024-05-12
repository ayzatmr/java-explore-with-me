package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.CategoryService;
import ru.practicum.common.dto.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid CategoryDto newCategoryDto) {
        return categoryService.create(newCategoryDto);
    }

    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto patch(@RequestBody @Valid CategoryDto newCategoryDto,
                             @Positive @PathVariable Long categoryId) {
        return categoryService.patch(categoryId, newCategoryDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void delete(@Positive @PathVariable Long categoryId) {
        categoryService.delete(categoryId);
    }
}
