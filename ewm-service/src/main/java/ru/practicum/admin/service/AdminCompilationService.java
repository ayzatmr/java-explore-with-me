package ru.practicum.admin.service;


import ru.practicum.admin.dto.NewCompilationDto;
import ru.practicum.admin.dto.UpdateCompilationRequestDto;
import ru.practicum.common.dto.CompilationDto;

public interface AdminCompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateRequest);

    void deleteCompilation(Long compId);
}
