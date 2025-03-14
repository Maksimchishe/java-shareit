package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestGetDto saveRequest(@RequestBody @Valid RequestCreateDto requestCreateDto,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.saveRequest(requestCreateDto, userId);
    }
}
