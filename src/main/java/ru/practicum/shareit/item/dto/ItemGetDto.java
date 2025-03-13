package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingGetSimplifiedDTO;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemGetDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private long owner;
    private long request;
    private List<CommentGetDto> comments;
    private BookingGetSimplifiedDTO lastBooking;
    private BookingGetSimplifiedDTO nextBooking;
}

