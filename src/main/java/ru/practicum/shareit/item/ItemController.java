package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public Set<ItemGetDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{id}")
    public ItemGetDto getItemById(@PathVariable long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public ItemGetDto createItem(@RequestBody @Valid ItemCreateDto itemCreateDto,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.createItem(itemCreateDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemGetDto updateItem(@RequestBody @Valid ItemUpdateDto itemUpdateDto,
                                 @PathVariable @Valid @NonNull Long id,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemUpdateDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@Valid @NonNull Long id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    public Set<ItemGetDto> getSearchItem(@RequestParam() String text) {
        return itemService.getSearchItem(text);
    }
}
