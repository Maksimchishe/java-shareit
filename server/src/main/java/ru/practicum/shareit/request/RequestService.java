package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

import java.util.List;

public interface RequestService {

    RequestGetDto getRequestById(long userId, long requestId);

    List<RequestGetDto> getRequestByRequesterId(long userId);

    RequestGetDto saveRequest(RequestCreateDto requestCreateDto, long userId);
}