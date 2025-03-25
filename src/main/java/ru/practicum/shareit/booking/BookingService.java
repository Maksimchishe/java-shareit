package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;

import java.util.List;

public interface BookingService {

    List<BookingGetDto> findAll();

    BookingGetDto getBookingById(long bookingId, long userId);

    BookingGetDto saveBooking(BookingCreateDto bookingCreateDto, long userId);

    BookingGetDto approvedBooking(long bookingId, boolean approved, long userId);
}