package ru.practicum.shareit.item;

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
    public ItemGetDto saveItem(@RequestBody ItemCreateDto itemCreateDto,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.saveItem(itemCreateDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemGetDto updateItem(@RequestBody ItemUpdateDto itemUpdateDto,
                                 @PathVariable Long id,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemUpdateDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.deleteById(id, userId);
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
