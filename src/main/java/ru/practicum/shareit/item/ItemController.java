package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemGetDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ItemGetDto getItemById(@PathVariable long id,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemById(id, userId);
    }

    @PostMapping
    public ItemGetDto saveItem(@RequestBody @Valid ItemCreateDto itemCreateDto,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.saveItem(itemCreateDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemGetDto updateItem(@RequestBody @Valid ItemUpdateDto itemUpdateDto,
                                 @PathVariable @Valid @NonNull Long id,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemUpdateDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@Valid @NonNull Long id) {
        itemService.deleteById(id);
    }

    @GetMapping("/search")
    public List<ItemGetDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentGetDto saveComment(@PathVariable long itemId,
                                     @RequestBody CommentCreateDto commentCreateDto,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.saveComment(itemId, commentCreateDto, userId);
    }
}
