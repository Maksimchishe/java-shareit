package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

public interface RequestService {
    RequestGetDto saveRequest(RequestCreateDto requestCreateDto, long userId);
}