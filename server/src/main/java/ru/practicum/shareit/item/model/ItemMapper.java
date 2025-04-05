package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemGetDto itemToGetDto(Item item);

    Item itemToCreateDto(ItemCreateDto itemCreateDto);

    Item itemToUpdateDto(ItemUpdateDto itemUpdateDto);
}
