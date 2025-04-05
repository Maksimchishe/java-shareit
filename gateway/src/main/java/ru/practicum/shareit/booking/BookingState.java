package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingState {
    ALL,
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
