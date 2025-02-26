package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
public class ItemRequest {
    long id; //— уникальный идентификатор запроса;
    String description; // — текст запроса, содержащий описание требуемой вещи;
    Long requestor; // — пользователь, создавший запрос;
    LocalDate created; // — дата и время создания запроса.
}
