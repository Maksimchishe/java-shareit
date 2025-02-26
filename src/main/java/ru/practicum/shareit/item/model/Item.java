package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    long id; // — уникальный идентификатор вещи;
    String name; // — краткое название;
    String description; // — развёрнутое описание;
    boolean available; // — статус о том, доступна или нет вещь для аренды;
    long owner; // — владелец вещи;
    ItemRequest request; /* — если вещь была создана по запросу другого пользователя,
                      то в этом поле будет храниться ссылка на соответствующий запрос.*/
}
