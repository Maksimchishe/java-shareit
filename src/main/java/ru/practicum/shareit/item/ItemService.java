package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.ItemMapper;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Set<ItemGetDto> getItems(long userId) {
        return itemRepository.getItems().stream()
                .filter(i -> i.getOwner() == userId)
                .map(ItemMapper.INSTANCE::itemToItemGetDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ItemGetDto getItemById(long id) {
        return ItemMapper.INSTANCE.itemToItemGetDto(itemRepository.getItemById(id));
    }

    public ItemGetDto createItem(ItemCreateDto itemCreateDto, long userId) {
        return ItemMapper.INSTANCE.itemToItemGetDto(
                itemRepository.createItem(ItemMapper.INSTANCE.itemToItemCreateDto(itemCreateDto), userId));
    }

    public ItemGetDto updateItem(ItemUpdateDto itemUpdateDto, long id, long userId) {
        return ItemMapper.INSTANCE.itemToItemGetDto(
                itemRepository.updateItem(ItemMapper.INSTANCE.itemToItemUpdateDto(itemUpdateDto), id, userId));
    }

    public void deleteItem(long id) {
        itemRepository.deleteItem(id);
    }

    public Set<ItemGetDto> getSearchItem(String text) {
        if (text.isEmpty()) {
            return new HashSet<>();
        }
        return itemRepository.getSearchItem(text).stream()
                .map(ItemMapper.INSTANCE::itemToItemGetDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

