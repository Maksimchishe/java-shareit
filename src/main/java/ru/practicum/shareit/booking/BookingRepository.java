package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    default Booking getBookingById(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Booking не найден."));
    }

    @Query("SELECT all_booking " +
           "FROM Booking AS all_booking " +
           "WHERE all_booking.booker.id = ?1 " +
           "AND all_booking.item.id = ?2 " +
           "AND all_booking.end < ?3 ")
    List<Booking> findAllByBookerAndItem(long userId, long itemId, LocalDateTime currentTime);

    @Query("SELECT booking " +
           "FROM Booking AS booking " +
           "WHERE booking.item.id = ?1 " +
           "AND booking.end < ?2 " +
           "ORDER BY booking.start DESC")
    Optional<Booking> findLastBooking(long itemId, LocalDateTime currentTime);

    @Query("SELECT booking " +
           "FROM Booking AS booking " +
           "WHERE booking.item.id = ?1 " +
           "AND booking.start > ?2 " +
           "ORDER BY booking.start")
    Optional<Booking> findNextBooking(long itemId, LocalDateTime currentTime);
}