package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.findAll(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable long id,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItemById(id, userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestBody @Valid ItemCreateDto itemCreateDto,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.saveItem(userId, itemCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Valid @NonNull Long id,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        itemClient.deleteById(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@PathVariable long itemId,
                                              @RequestBody CommentCreateDto commentCreateDto,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.saveComment(itemId, commentCreateDto, userId);
    }
}
