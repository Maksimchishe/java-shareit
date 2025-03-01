package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
}

