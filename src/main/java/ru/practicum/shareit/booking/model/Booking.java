package ru.practicum.shareit.booking.model;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    long id; //— уникальный идентификатор бронирования;
    LocalDate start; //— дата и время начала бронирования;
    LocalDate end; //— дата и время конца бронирования;
    String item; //— вещь, которую пользователь бронирует;
    String booker; //— пользователь, который осуществляет бронирование;
    String status; /*— статус бронирования. Может принимать одно из следующих значений:
                        WAITING — новое бронирование, ожидает одобрения,
                        APPROVED — Дополнительные советы ментора бронирование подтверждено владельцем,
                        REJECTED — бронирование отклонено владельцем,
                        CANCELED — бронирование отменено создателем.*/
}
