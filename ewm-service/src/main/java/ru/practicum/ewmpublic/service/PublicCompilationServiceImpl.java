package ru.practicum.ewmpublic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.model.Compilation;
import ru.practicum.admin.repository.CompilationRepository;
import ru.practicum.common.dto.CompilationDto;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.mapper.CompilationMapper;
import ru.practicum.common.model.Constants;
import ru.practicum.common.pagination.CustomPageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, int from, int size) {
        CustomPageRequest pageRequest = CustomPageRequest.of(from, size, Constants.ID_SORTING);
        return compilationMapper.toDtoList(compilationRepository.findAllWithPinned(pinned, pageRequest));
    }

    @Override
    public CompilationDto findCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findCompilationWithEventById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation is not found"));
        return compilationMapper.toDto(compilation);
    }
}
