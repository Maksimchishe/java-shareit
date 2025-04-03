package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT all_booking
            FROM Booking AS all_booking
            WHERE all_booking.booker.id = ?1
            AND all_booking.item.id = ?2
            AND all_booking.end < ?3
            """)
    List<Booking> findAllByBookerAndItem(long userId, long itemId, LocalDateTime currentTime);

    @Query("""
            SELECT booking
            FROM Booking AS booking
            WHERE booking.item.id = ?1
            AND booking.end < ?2
            ORDER BY booking.start DESC
            """)
    Optional<Booking> findLastBooking(long itemId, LocalDateTime currentTime);

    @Query("""
            SELECT booking
            FROM Booking AS booking
            WHERE booking.item.id = ?1
            AND booking.start > ?2
            ORDER BY booking.start
            """)
    Optional<Booking> findNextBooking(long itemId, LocalDateTime currentTime);

    List<Booking> findAllByItemOwnerOrderByStartDesc(long userId);

    @Query("SELECT all_booking " +
           "FROM Booking AS all_booking " +
           "WHERE all_booking.item.owner = ?1 " +
           "AND (?2 BETWEEN all_booking.start AND all_booking.end) " +
           "ORDER BY all_booking.start DESC")
    List<Booking> findAllByOwnerCurrentState(long userId, LocalDateTime currentTime);

    @Query("SELECT all_booking " +
           "FROM Booking AS all_booking " +
           "WHERE all_booking.item.owner = ?1 " +
           "AND all_booking.end < ?2 " +
           "ORDER BY all_booking.start DESC")
    List<Booking> findAllByOwnerPastState(long userId, LocalDateTime currentTime);

    @Query("SELECT all_booking " +
           "FROM Booking AS all_booking " +
           "WHERE all_booking.item.owner = ?1 " +
           "AND all_booking.start > ?2 " +
           "ORDER BY all_booking.start DESC")
    List<Booking> findAllByOwnerFutureState(long userId, LocalDateTime currentTime);

    @Query("SELECT all_booking " +
           "FROM Booking AS all_booking " +
           "WHERE all_booking.item.owner = ?1 " +
           "AND all_booking.status = ?2 " +
           "ORDER BY all_booking.start DESC")
    List<Booking> findAllByOwnerAndStatus(long userId, BookingState status);
}