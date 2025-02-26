package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class ItemUpdateDto {
    long id; // — уникальный идентификатор вещи;
    String name; // — краткое название;
    String description; // — развёрнутое описание;
    Boolean available; // — статус о том, доступна или нет вещь для аренды;
}

