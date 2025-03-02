package ru.practicum.shareit.request;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RequestRepository {

    private long id;
    private final Map<Long, Request> itemRequestMap = new HashMap<>();

    private long nextId() {
        return id++;
    }

    public List<Request> getRequests() {
        return itemRequestMap.values().stream().toList();
    }

    public Request getRequestById(long id) {
        if (!itemRequestMap.containsKey(id)) {
            throw new NotFoundException("Запрос не найдена.");
        }
        return itemRequestMap.get(id);
    }

    public Request createRequest(Request request) {
        long id = nextId();
        request.setId(id);
        itemRequestMap.put(id, request);
        return itemRequestMap.get(id);
    }

    public Request updateRequest(Request request, long id) {
        Request updateRequest = getRequestById(id);
        if (request.getDescription() != null) {
            updateRequest.setDescription(request.getDescription());
        }
        if (request.getRequestor() != null) {
            updateRequest.setRequestor(request.getRequestor());
        }
        itemRequestMap.put(id, updateRequest);
        return itemRequestMap.get(id);
    }

    public void deleteRequest(long id) {
        getRequestById(id);
        itemRequestMap.remove(id);
    }
}

