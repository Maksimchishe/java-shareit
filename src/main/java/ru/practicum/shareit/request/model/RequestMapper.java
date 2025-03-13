package ru.practicum.shareit.request.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

@Mapper
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    RequestGetDto requestToGetDto(Request request);

    Request createToRequestDto(RequestCreateDto requestCreateDto);
}