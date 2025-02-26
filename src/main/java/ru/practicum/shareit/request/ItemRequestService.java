package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestUploadeDto;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;

    public Set<ItemRequestGetDto> getItemRequests() {
        return itemRequestStorage.getItemRequests().stream()
                .map(ItemRequestMapper::toGetItemRequestDto)
                .sorted(Comparator.comparing(ItemRequestGetDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ItemRequestGetDto getItemRequestById(long id) {
        return ItemRequestMapper.toGetItemRequestDto(itemRequestStorage.getItemRequestById(id));
    }

    public ItemRequestGetDto createItemRequest(ItemRequestCreateDto itemRequestCreateDto) {
        return ItemRequestMapper.toGetItemRequestDto(itemRequestStorage.createItemRequest(
                ItemRequestMapper.createDtoToItemRequest(itemRequestCreateDto)));
    }

    public ItemRequestGetDto updateItemRequest(ItemRequestUploadeDto itemRequestUploadeDto, long id) {
        return ItemRequestMapper.toGetItemRequestDto(itemRequestStorage.updateItemRequest(
                ItemRequestMapper.updateDtoToItemRequest(itemRequestUploadeDto), id));
    }

    public void deleteItemRequest(long id) {
        itemRequestStorage.deleteItemRequest(id);
    }
}
