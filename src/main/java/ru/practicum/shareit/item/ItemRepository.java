package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRepository implements ItemStorage {

    private final UserStorage userStorage;

    private long id;
    private final Map<Long, Item> itemMap = new HashMap<>();

    private long nextId() {
        return id++;
    }

    @Override
    public List<Item> getItems() {
        return itemMap.values().stream().toList();
    }

    @Override
    public Item getItemById(long id) {
        if (!itemMap.containsKey(id)) {
            throw new NotFoundException("Вещь не найдена.");
        }
        return itemMap.get(id);
    }

    @Override
    public Item createItem(Item item, long userId) {
        userStorage.getUserById(userId);
        long id = nextId();
        item.setId(id);
        item.setOwner(userId);
        itemMap.put(id, item);
        return itemMap.get(id);
    }

    @Override
    public Item updateItem(Item item, long id, long userId) {
        userStorage.getUserById(userId);
        Item updateItem = getItemById(id);

        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        updateItem.setAvailable(item.isAvailable());
        itemMap.put(id, updateItem);
        return itemMap.get(id);
    }

    @Override
    public void deleteItem(long id) {
        getItemById(id);
        itemMap.remove(id);
    }

    @Override
    public List<Item> getSearchItem(String text) {
        return itemMap.values().stream()
                .filter(Item::isAvailable)
                .filter(i ->
                        (i.getName() + i.getDescription())
                                .replaceAll("\\s", "").toUpperCase().contains(text)
                )
                .toList();
    }
}