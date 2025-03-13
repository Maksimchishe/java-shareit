package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingGetSimplifiedDTO;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingGetDto bookingToGetDto(Booking booking);

    Booking bookingToCreateDto(BookingCreateDto bookingCreateDto);

    BookingGetSimplifiedDTO bookingToSimpleDto(Booking booking);

}
