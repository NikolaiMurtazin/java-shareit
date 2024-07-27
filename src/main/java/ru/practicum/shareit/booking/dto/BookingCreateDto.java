package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validation.ValidBookingDates;

import java.time.LocalDateTime;

@Data
@Builder
@ValidBookingDates
public class BookingCreateDto {
    @NotNull(message = "Booking start can't be blank")
    private LocalDateTime start;

    @NotNull(message = "Booking end can't be blank")
    private LocalDateTime end;

    @NotNull(message = "Item id can't be blank")
    private Long itemId;
}
