package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    List<Item> getItems();

    Item getItemById(long id);

    Item createItem(Item item, long userId);

    Item updateItem(Item item, long id, long userId);

    void deleteItem(long id);

    List<Item> getSearchItem(String text);
}
