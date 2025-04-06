package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingGetDto> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::bookingToGetDto)
                .sorted(Comparator.comparing(BookingGetDto::getId).reversed())
                .toList();
    }

    @Override
    public BookingGetDto getBookingByBookerId(long bookingId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User не найден."));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking не найден."));

        if (booking.getItem().getOwner() == user.getId()
            || booking.getBooker().getId() == user.getId()) {
            return bookingMapper.bookingToGetDto(booking);
        } else {
            throw new NotFoundException("User не владелец и не заказчик Item");
        }
    }

    @Override
    public List<BookingGetDto> getAllByOwnerId(long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }

        return switch (state) {
            case "ALL" -> bookingRepository.findAllByItemOwnerOrderByStartDesc(userId).stream()
                    .map(bookingMapper::bookingToGetDto)
                    .toList();
            case "CURRENT" -> bookingRepository.findAllByOwnerCurrentState(userId, LocalDateTime.now()).stream()
                    .map(bookingMapper::bookingToGetDto)
                    .toList();
            case "PAST" -> bookingRepository.findAllByOwnerPastState(userId, LocalDateTime.now()).stream()
                    .map(bookingMapper::bookingToGetDto)
                    .toList();
            case "FUTURE" -> bookingRepository.findAllByOwnerFutureState(userId, LocalDateTime.now()).stream()
                    .map(bookingMapper::bookingToGetDto)
                    .toList();
            case "WAITING" -> bookingRepository.findAllByOwnerAndStatus(userId, BookingState.WAITING).stream()
                    .map(bookingMapper::bookingToGetDto)
                    .toList();
            case "REJECTED" -> bookingRepository.findAllByOwnerAndStatus(userId, BookingState.REJECTED).stream()
                    .map(bookingMapper::bookingToGetDto)
                    .toList();
            default -> throw new ValidationException("Неправильный статус.");
        };
    }

    @Override
    @Transactional
    public BookingGetDto saveBooking(BookingCreateDto bookingCreateDto, long userId) {
        Booking booking = bookingMapper.bookingToCreateDto(bookingCreateDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User не найден."));
        booking.setBooker(user);

        Item item = itemRepository.findById(bookingCreateDto.getItemId()).orElseThrow(() -> new NotFoundException("Item не найден."));
        if (!item.isAvailable()) {
            throw new ValidationException("isAvailable = false");
        }
        booking.setItem(item);
        booking.setStatus(BookingState.WAITING);

        return bookingMapper.bookingToGetDto(
                bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingGetDto approvedBooking(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking не найден."));

        if (booking.getItem().getOwner() == userId) {
            if (approved && booking.getStatus().equals(BookingState.APPROVED)) {
                throw new ValidationException("Бронирование уже одобрено.");
            } else if (approved) {
                booking.setStatus(BookingState.APPROVED);
                bookingRepository.save(booking);
            } else {
                booking.setStatus(BookingState.REJECTED);
                bookingRepository.save(booking);
            }
        } else if (booking.getBooker().getId() == userId) {
            if (!approved) {
                booking.setStatus(BookingState.CANCELED);
                bookingRepository.save(booking);
            }
        } else {
            throw new ValidationException("User не владелец и не заказчик Item");
        }
        return bookingMapper.bookingToGetDto(booking);
    }

}