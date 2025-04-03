package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateDto {
    private String description;
    private long requester;
    private LocalDateTime created;
}
