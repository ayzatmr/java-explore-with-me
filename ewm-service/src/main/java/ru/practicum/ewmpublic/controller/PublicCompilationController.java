package ru.practicum.ewmpublic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.dto.CompilationDto;
import ru.practicum.ewmpublic.service.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.model.Constants.MAX_SIZE;
import static ru.practicum.common.model.Constants.MIN_SIZE;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final PublicCompilationService compilationService;


    @GetMapping
    public List<CompilationDto> findAll(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = MIN_SIZE) @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size) {
        return compilationService.findCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable Long compId) {
        return compilationService.findCompilationById(compId);
    }
}
