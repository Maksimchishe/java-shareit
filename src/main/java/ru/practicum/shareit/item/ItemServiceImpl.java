package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    public Set<ItemGetDto> getItems(long userId) {
        return itemStorage.getItems().stream()
                .filter(i -> i.getOwner() == userId)
                .map(ItemMapper::toGetItemDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ItemGetDto getItemById(long id) {
        return ItemMapper.toGetItemDto(itemStorage.getItemById(id));
    }

    public ItemGetDto createItem(ItemCreateDto itemCreateDto, long userId) {
        return ItemMapper.toGetItemDto(itemStorage.createItem(ItemMapper.createDtoToItem(itemCreateDto), userId));
    }

    public ItemGetDto updateItem(ItemUpdateDto itemUpdateDto, long id, long userId) {
        return ItemMapper.toGetItemDto(itemStorage.updateItem(ItemMapper.updateDtoToItem(itemUpdateDto), id, userId));
    }

    public void deleteItem(long id) {
        itemStorage.deleteItem(id);
    }

    public Set<ItemGetDto> getSearchItem(String text) {
        return itemStorage.getSearchItem(text).stream()
                .map(ItemMapper::toGetItemDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
