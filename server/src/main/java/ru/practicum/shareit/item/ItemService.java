package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    List<ItemGetDto> findAll(long userId);

    ItemGetDto getItemById(long id, long userId);

    ItemGetDto saveItem(ItemCreateDto itemCreateDto, long userId);

    ItemGetDto updateItem(ItemUpdateDto itemUpdateDto, long id, long userId);

    void deleteById(long id, long userId);

    List<ItemGetDto> search(String text);

    CommentGetDto saveComment(long userId, CommentCreateDto commentCreateDto, long itemId);
}