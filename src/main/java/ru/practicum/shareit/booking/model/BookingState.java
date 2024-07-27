package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL, // все
    CURRENT, // текущие
    PAST, // завершённые
    FUTURE, // будущие
    WAITING, // ожидающие подтверждения
    REJECTED; // отклонённые

    static BookingState from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equalsIgnoreCase(state)) {
                return value;
            }
        }
        return null;
    }
}
