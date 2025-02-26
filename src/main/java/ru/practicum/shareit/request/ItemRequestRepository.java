package ru.practicum.shareit.request;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRequestRepository implements ItemRequestStorage {

    private long id;
    private final Map<Long, ItemRequest> itemRequestMap = new HashMap<>();

    private long nextId() {
        return id++;
    }

    @Override
    public List<ItemRequest> getItemRequests() {
        return itemRequestMap.values().stream().toList();
    }

    @Override
    public ItemRequest getItemRequestById(long id) {
        if (!itemRequestMap.containsKey(id)) {
            throw new NotFoundException("Запрос не найдена.");
        }
        return itemRequestMap.get(id);
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        long id = nextId();
        itemRequest.setId(id);
        itemRequestMap.put(id, itemRequest);
        return itemRequestMap.get(id);
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest, long id) {
        ItemRequest updateItemRequest = getItemRequestById(id);
        if (itemRequest.getDescription() != null) {
            updateItemRequest.setDescription(itemRequest.getDescription());
        }
        if (itemRequest.getRequestor() != null) {
            updateItemRequest.setRequestor(itemRequest.getRequestor());
        }
        itemRequestMap.put(id, updateItemRequest);
        return itemRequestMap.get(id);
    }

    @Override
    public void deleteItemRequest(long id) {
        getItemRequestById(id);
        itemRequestMap.remove(id);
    }
}

