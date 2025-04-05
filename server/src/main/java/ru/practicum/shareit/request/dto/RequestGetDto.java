package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemGetDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestGetDto {
    private long id;
    private String description;
    private long requester;
    private List<ItemGetDto> items;
    private LocalDateTime created;
}
