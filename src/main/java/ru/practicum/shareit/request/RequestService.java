package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

interface RequestService {
    RequestGetDto saveRequest(RequestCreateDto requestCreateDto, long userId);
}