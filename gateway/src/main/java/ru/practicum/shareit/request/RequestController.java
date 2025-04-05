package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getRequestByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequestByOwnerId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequestById(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> saveRequest(@RequestBody @Valid RequestCreateDto requestCreateDto,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.saveRequest(userId, requestCreateDto);
    }
}
