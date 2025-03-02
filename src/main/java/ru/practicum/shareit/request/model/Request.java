package ru.practicum.shareit.request.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class Request {
    private long id;
    private String description;
    private Long requestor;
    private LocalDate created;
}
