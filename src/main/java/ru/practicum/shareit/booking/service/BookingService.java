package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingDto add(long userId, BookingCreateDto bookingCreateDto);

    BookingDto changeStatus(long userId, long bookingId, boolean approved);

    BookingDto getById(long userId, long bookingId);

//    Collection<BookingDto> getByBookerId(long bookerId, BookingState state);
//
//    Collection<BookingDto> getByOwnerId(long ownerId, BookingState state);
}
