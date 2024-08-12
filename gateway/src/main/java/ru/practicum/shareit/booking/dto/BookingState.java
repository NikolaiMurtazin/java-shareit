package ru.practicum.shareit.booking.dto;

import jakarta.validation.ValidationException;

public enum BookingState {
	// Все
	ALL,
	// Текущие
	CURRENT,
	// Будущие
	FUTURE,
	// Завершенные
	PAST,
	// Отклоненные
	REJECTED,
	// Ожидающие подтверждения
	WAITING;

	public static BookingState from(String state) {
		for (BookingState bookingState : BookingState.values()) {
			if (bookingState.name().equalsIgnoreCase(state)) {
				return bookingState;
			}
		}
		throw new ValidationException("Unknown state: " + state);
	}
}
