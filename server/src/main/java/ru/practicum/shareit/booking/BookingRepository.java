package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT all_booking
            FROM Booking AS all_booking
            WHERE all_booking.booker.id = :userId
            AND all_booking.item.id = :itemId
            AND all_booking.end < :currentTime
            """)
    List<Booking> findAllByBookerAndItem(@Param("userId") long userId,
                                         @Param("itemId") long itemId,
                                         @Param("currentTime") LocalDateTime currentTime);

    @Query("""
            SELECT booking
            FROM Booking AS booking
            WHERE booking.item.id = :itemId
            AND booking.end < :currentTime
            ORDER BY booking.start DESC
            """)
    Optional<Booking> findLastBooking(@Param("itemId") long itemId,
                                      @Param("currentTime") LocalDateTime currentTime);

    @Query("""
            SELECT booking
            FROM Booking AS booking
            WHERE booking.item.id = :itemId
            AND booking.start > :currentTime
            ORDER BY booking.start
            """)
    Optional<Booking> findNextBooking(@Param("itemId") long itemId,
                                      @Param("currentTime") LocalDateTime currentTime);

    List<Booking> findAllByItemOwnerOrderByStartDesc(long userId);

    @Query("""
            SELECT all_booking
            FROM Booking AS all_booking
            WHERE all_booking.item.owner = :userId
            AND (:currentTime BETWEEN all_booking.start AND all_booking.end)
            ORDER BY all_booking.start DESC
            """)
    List<Booking> findAllByOwnerCurrentState(@Param("userId") long userId,
                                             @Param("currentTime") LocalDateTime currentTime);

    @Query("""
            SELECT all_booking
            FROM Booking AS all_booking
            WHERE all_booking.item.owner = :userId
            AND all_booking.end < :currentTime
            ORDER BY all_booking.start DESC
            """)
    List<Booking> findAllByOwnerPastState(@Param("userId") long userId,
                                          @Param("currentTime") LocalDateTime currentTime);

    @Query("""
            SELECT all_booking
            FROM Booking AS all_booking
            WHERE all_booking.item.owner = :userId
            AND all_booking.start > :currentTime
            ORDER BY all_booking.start DESC
            """)
    List<Booking> findAllByOwnerFutureState(@Param("userId") long userId,
                                            @Param("currentTime") LocalDateTime currentTime);

    @Query("""
            SELECT all_booking
            FROM Booking AS all_booking
            WHERE all_booking.item.owner = :userId
            AND all_booking.status = :status
            ORDER BY all_booking.start DESC
            """)
    List<Booking> findAllByOwnerAndStatus(@Param("userId") long userId,
                                          @Param("status") BookingState status);
}