package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;
import ru.practicum.shareit.request.dto.RequestUpdateDto;

import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    final RequestService requestService;

    @GetMapping
    public Set<RequestGetDto> getItemRequests() {
        return requestService.getItemRequests();
    }

    @GetMapping("/{id}")
    public RequestGetDto getItemRequestById(@PathVariable long id) {
        return requestService.getItemRequestById(id);
    }

    @PostMapping
    public RequestGetDto createItemRequest(@RequestBody @Valid RequestCreateDto requestCreateDto) {
        return requestService.createItemRequest(requestCreateDto);
    }

    @PatchMapping("/{id}")
    public RequestGetDto updateItemRequest(@RequestBody @Valid RequestUpdateDto requestUpdateDto, @PathVariable @Valid @NonNull Long id) {
        return requestService.updateItemRequest(requestUpdateDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteItemRequest(Long id) {
        if (id != null) {
            requestService.deleteItemRequest(id);
        }
    }

}
