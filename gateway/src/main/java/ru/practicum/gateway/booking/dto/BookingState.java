package ru.practicum.gateway.booking.dto;

import java.util.Optional;

public enum BookingState {

    ALL,

    CURRENT,

    PAST,

    FUTURE,

    WAITING,

    REJECTED,

    UNSUPPORTED_STATUS;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state: values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
