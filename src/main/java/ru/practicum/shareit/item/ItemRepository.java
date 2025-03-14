package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    default Item getItemById(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Item не найден."));
    }

    default void existsItemById(long id) {
        if (!existsById(id)) {
            throw new NotFoundException("Item не найден.");
        }
    }

    @Query("""
            select i from Item i where
            (upper(i.name) like upper(concat('%', ?1, '%'))
            or upper(i.description) like upper(concat('%', ?1, '%')))
            and i.available = true
            """)
    List<Item> search(String text);
}