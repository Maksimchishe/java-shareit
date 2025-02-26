package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Set;

public interface ItemService {

    Set<ItemGetDto> getItems(long userId);

    ItemGetDto getItemById(long id);

    ItemGetDto createItem(ItemCreateDto itemCreateDto, long userId);

    ItemGetDto updateItem(ItemUpdateDto itemUpdateDto, long id, long userId);

    void deleteItem(long id);

    Set<ItemGetDto> getSearchItem(String text);
}
