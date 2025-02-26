package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {

    public static ItemRequestGetDto toGetItemRequestDto(ItemRequest itemRequest) {
        ItemRequestGetDto itemRequestGetDto = new ItemRequestGetDto();
        itemRequestGetDto.setId(itemRequest.getId());
        itemRequestGetDto.setDescription(itemRequest.getDescription());
        itemRequestGetDto.setRequestor(itemRequest.getRequestor());
        itemRequestGetDto.setCreated(itemRequest.getCreated());
        return itemRequestGetDto;
    }

    public static ItemRequest createDtoToItemRequest(ItemRequestCreateDto itemRequestCreateDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestCreateDto.getDescription());
        itemRequest.setRequestor(itemRequestCreateDto.getRequestor());
        itemRequest.setCreated(itemRequestCreateDto.getCreated());
        return itemRequest;
    }

    public static ItemRequest updateDtoToItemRequest(ItemRequestUploadeDto itemRequestUploadeDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestUploadeDto.getDescription());
        itemRequest.setRequestor(itemRequestUploadeDto.getRequestor());
        itemRequest.setCreated(itemRequestUploadeDto.getCreated());
        return itemRequest;
    }
}
