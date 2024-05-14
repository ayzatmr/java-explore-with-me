package ru.practicum.ewmpublic.service;


import ru.practicum.common.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> findCompilations(Boolean pinned, int from, int size);

    CompilationDto findCompilationById(Long compId);
}
