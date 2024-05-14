package ru.practicum.ewmpublic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.dto.CategoryDto;
import ru.practicum.ewmpublic.service.PublicCategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.model.Constants.MAX_SIZE;
import static ru.practicum.common.model.Constants.MIN_SIZE;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/categories")
public class PublicCategoriesController {
    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> findAll(@RequestParam(defaultValue = MIN_SIZE) @PositiveOrZero int from,
                                     @RequestParam(defaultValue = MAX_SIZE) @Positive int size) {
        return publicCategoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto get(@PathVariable @Positive Long catId) {
        return publicCategoryService.get(catId);
    }
}
