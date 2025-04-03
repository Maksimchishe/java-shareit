package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingGetSimplifiedDTO;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingGetDto bookingToGetDto(Booking booking);

    Booking bookingToCreateDto(BookingCreateDto bookingCreateDto);

    BookingGetSimplifiedDTO bookingToSimpleDto(Booking booking);

}
