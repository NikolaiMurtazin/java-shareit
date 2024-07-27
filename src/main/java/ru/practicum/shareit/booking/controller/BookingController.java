package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.constants.UserIdHttpHeader.USER_ID_HEADER;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@RequestHeader(USER_ID_HEADER) long userId,
                            @Validated @RequestBody BookingCreateDto bookingCreateDto) {
        return bookingService.add(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@RequestHeader(USER_ID_HEADER) long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.changeStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(USER_ID_HEADER) long userId,
                                @PathVariable long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

//    @GetMapping
//    public Collection<BookingDto> getByBookerId(@RequestHeader(USER_ID_HEADER) long bookerId,
//                                    @RequestParam(defaultValue = "ALL") String state) {
//        return bookingService.getByBookerId(bookerId, BookingState.valueOf(state));
//    }
//
//
//    @GetMapping("/owner")
//    public Collection<BookingDto> getByOwnerId(@RequestHeader(USER_ID_HEADER) long ownerId,
//                                                @RequestParam(defaultValue = "ALL") String state) {
//        return bookingService.getByOwnerId(ownerId, BookingState.valueOf(state));
//    }
}
