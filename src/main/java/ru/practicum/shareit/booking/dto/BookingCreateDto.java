package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
public class BookingCreateDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
