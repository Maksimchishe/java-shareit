package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingGetDto> findAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{bookingId}")
    public BookingGetDto getBookingByBookerId(@PathVariable long bookingId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingByBookerId(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingGetDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByOwnerId(userId, state);
    }

    @PostMapping
    public BookingGetDto saveBooking(@RequestBody BookingCreateDto bookingCreateDto,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.saveBooking(bookingCreateDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingGetDto approvedBooking(@PathVariable long bookingId,
                                         @RequestParam(name = "approved") boolean approved,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approvedBooking(bookingId, approved, userId);
    }
}
