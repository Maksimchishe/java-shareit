package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    default Request getRequestById(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Request не найден."));
    }

    List<Request> findAllByRequesterOrderByCreatedDesc(long userId);
}