package ru.practicum.shareit.request.model;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestGetDto requestToGetDto(Request request);

    Request createToRequestDto(RequestCreateDto requestCreateDto);
}