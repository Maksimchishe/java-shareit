package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@NoArgsConstructor
public class RequestCreateDto {
    private String description;
    private User requester;
    private LocalDateTime created;
}
