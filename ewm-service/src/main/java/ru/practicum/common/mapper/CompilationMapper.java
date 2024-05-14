package ru.practicum.common.mapper;

import org.mapstruct.Mapper;
import ru.practicum.admin.model.Compilation;
import ru.practicum.common.dto.CompilationDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);

    List<CompilationDto> toDtoList(List<Compilation> compilations);
}
