package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.dto.ItemRequestUploadeDto;

import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    final ItemRequestService itemRequestService;

    @GetMapping
    public Set<ItemRequestGetDto> getItemRequests() {
        return itemRequestService.getItemRequests();
    }

    @GetMapping("/{id}")
    public ItemRequestGetDto getItemRequestById(@PathVariable long id) {
        return itemRequestService.getItemRequestById(id);
    }

    @PostMapping
    public ItemRequestGetDto createItemRequest(@RequestBody @Valid ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestService.createItemRequest(itemRequestCreateDto);
    }

    @PatchMapping("/{id}")
    public ItemRequestGetDto updateItemRequest(@RequestBody @Valid ItemRequestUploadeDto itemRequestUploadeDto, @PathVariable @Valid @NonNull Long id) {
        return itemRequestService.updateItemRequest(itemRequestUploadeDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteItemRequest(Long id) {
        if (id != null) {
            itemRequestService.deleteItemRequest(id);
        }
    }

}
