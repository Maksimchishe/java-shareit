package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemGetDto itemToItemGetDto(Item item);

    Item itemToItemCreateDto(ItemCreateDto itemCreateDto);

    Item itemToItemUpdateDto(ItemUpdateDto itemUpdateDto);
}
