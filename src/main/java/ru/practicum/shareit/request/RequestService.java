package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;
import ru.practicum.shareit.request.dto.RequestUpdateDto;
import ru.practicum.shareit.request.model.RequestMapper;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public Set<RequestGetDto> getItemRequests() {
        return requestRepository.getRequests().stream()
                .map(RequestMapper.INSTANCE::requestToGetDto)
                .sorted(Comparator.comparing(RequestGetDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public RequestGetDto getItemRequestById(long id) {
        return RequestMapper.INSTANCE.requestToGetDto(
                requestRepository.getRequestById(id));
    }

    public RequestGetDto createItemRequest(RequestCreateDto requestCreateDto) {
        return RequestMapper.INSTANCE.requestToGetDto(
                requestRepository.createRequest(RequestMapper.INSTANCE.createToRequestDto(requestCreateDto)));
    }

    public RequestGetDto updateItemRequest(RequestUpdateDto requestUpdateDto, long id) {
        return RequestMapper.INSTANCE.requestToGetDto(
                requestRepository.updateRequest(RequestMapper.INSTANCE.updateToRequestDto(requestUpdateDto), id));
    }

    public void deleteItemRequest(long id) {
        requestRepository.deleteRequest(id);
    }
}
