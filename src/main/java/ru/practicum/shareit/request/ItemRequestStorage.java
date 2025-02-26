package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage {

    List<ItemRequest> getItemRequests();

    ItemRequest getItemRequestById(long id);

    ItemRequest createItemRequest(ItemRequest item);

    ItemRequest updateItemRequest(ItemRequest item, long id);

    void deleteItemRequest(long id);
}
