package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class ItemGetDto {
    long id; // — уникальный идентификатор вещи;
    @NotBlank
    String name; // — краткое название;
    @NotBlank
    String description; // — развёрнутое описание;
    @NotNull
    Boolean available; // — статус о том, доступна или нет вещь для аренды;
    Long owner;
    ItemRequest request;
}

