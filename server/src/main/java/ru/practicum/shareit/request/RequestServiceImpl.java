package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final RequestMapper requestMapper;


    private List<RequestGetDto> getRequestDto(List<RequestGetDto> requests, List<ItemGetDto> items) {
        Map<Long, RequestGetDto> requestDTOMap = new HashMap<>();

        for (RequestGetDto requestGetDto : requests) {
            requestDTOMap.put(requestGetDto.getId(), requestGetDto);
        }

        for (ItemGetDto itemGetDto : items) {
            if (requestDTOMap.containsKey(itemGetDto.getRequest())) {
                requestDTOMap.get(itemGetDto.getRequest()).getItems().add(itemGetDto);
            }
        }
        return new ArrayList<>(requestDTOMap.values());
    }

    @Override
    public List<RequestGetDto> getRequestByRequesterId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        List<RequestGetDto> requests = requestRepository.findAllByRequesterOrderByCreatedDesc(userId).stream()
                .map(requestMapper::requestToGetDto)
                .toList();
        List<ItemGetDto> items = itemRepository.findAllByOwner(userId).stream()
                .map(itemMapper::itemToGetDto)
                .toList();

        return getRequestDto(requests, items);
    }

    @Override
    public RequestGetDto getRequestById(long userId, long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        Request request = requestRepository.getRequestById(requestId);

        List<ItemGetDto> items = itemRepository.findAllByRequest(requestId).stream()
                .map(itemMapper::itemToGetDto)
                .toList();

        RequestGetDto requestGetDto = requestMapper.requestToGetDto(request);
        requestGetDto.setItems(items);

        return requestGetDto;
    }

    @Override
    @Transactional
    public RequestGetDto saveRequest(RequestCreateDto requestCreateDto, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        Request request = requestMapper.createToRequestDto(requestCreateDto);
        request.setCreated(LocalDateTime.now());
        request.setRequester(userId);

        return requestMapper.requestToGetDto(requestRepository.save(request));
    }
}