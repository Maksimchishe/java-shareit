package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
public class RequestCreateDto {
    private long id;
    private String description;
    private Long requestor;
    private LocalDate created;
}
