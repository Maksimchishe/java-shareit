package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingGetDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private BookingState status;
}

