package ru.practicum.shareit.booking;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED, UNSUPPORTED_STATUS;

    static BookingState from(String state) {
        for (BookingState value: BookingState.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}
