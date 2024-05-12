package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.dto.NewCompilationDto;
import ru.practicum.admin.dto.UpdateCompilationRequestDto;
import ru.practicum.admin.model.Compilation;
import ru.practicum.admin.repository.AdminEventRepository;
import ru.practicum.admin.repository.CompilationRepository;
import ru.practicum.common.dto.CompilationDto;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.mapper.CompilationMapper;
import ru.practicum.common.model.Event;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;

    private final AdminEventRepository eventRepository;

    private final CompilationMapper compilationMapper;


    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Long> compilationEventIds = newCompilationDto.getEvents();
        List<Event> compilationEvents = getCompilationEvents(newCompilationDto, compilationEventIds);
        Compilation compilation = Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(compilationEvents)
                .build();
        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateRequest) {
        Compilation compilation = getCompilationWithEvents(compId);
        updateCompilation(updateRequest, compilation);
        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        compilationRepository.deleteById(compId);
    }


    private void updateCompilation(UpdateCompilationRequestDto updateRequest, Compilation compilation) {
        if (updateRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateRequest.getEvents());
            bindEventsToCompilation(compilation, events);
            compilation.setEvents(events);
        }
        if (updateRequest.getTitle() != null) {
            compilation.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getPinned() != null) {
            compilation.setPinned(updateRequest.getPinned());
        }
    }

    private void bindEventsToCompilation(Compilation compilation, List<Event> events) {
        events.forEach(event -> event.addToCompilation(compilation));
        eventRepository.saveAll(events);
    }

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation is not found"));
    }

    private List<Event> getCompilationEvents(NewCompilationDto newCompilationDto, List<Long> compilationEventIds) {
        List<Event> compilationEvents;
        if (newCompilationDto.getEvents() != null) {
            compilationEvents = eventRepository.findAllById(compilationEventIds);
        } else {
            compilationEvents = Collections.emptyList();
        }
        return compilationEvents;
    }

    private Compilation getCompilationWithEvents(Long compId) {
        return compilationRepository.findCompilationWithEventById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation is not found"));
    }
}
