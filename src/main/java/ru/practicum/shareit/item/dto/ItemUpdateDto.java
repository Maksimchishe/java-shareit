package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.Request;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class ItemUpdateDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long owner;
    private Request request;
}

