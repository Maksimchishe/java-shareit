package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestGetDto> getRequestByRequesterId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestByRequesterId(userId);
    }

    @GetMapping("/{requestId}")
    public RequestGetDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long requestId) {
        return requestService.getRequestById(userId, requestId);
    }

    @PostMapping
    public RequestGetDto saveRequest(@RequestBody RequestCreateDto requestCreateDto,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.saveRequest(requestCreateDto, userId);
    }
}
