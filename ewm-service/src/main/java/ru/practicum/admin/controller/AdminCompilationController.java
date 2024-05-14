package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.NewCompilationDto;
import ru.practicum.admin.dto.UpdateCompilationRequestDto;
import ru.practicum.admin.service.AdminCompilationService;
import ru.practicum.common.dto.CompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminCompilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@Positive @PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationRequestDto updateRequest) {
        return adminCompilationService.updateCompilation(compId, updateRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        adminCompilationService.deleteCompilation(compId);
    }
}
