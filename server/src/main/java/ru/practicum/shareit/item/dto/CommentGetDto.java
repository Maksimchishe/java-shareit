package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentGetDto {
    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}