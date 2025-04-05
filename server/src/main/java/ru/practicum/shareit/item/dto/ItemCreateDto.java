package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateDto {
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
}

