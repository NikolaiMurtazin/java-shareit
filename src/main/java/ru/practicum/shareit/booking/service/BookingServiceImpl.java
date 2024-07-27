package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public BookingDto add(long userId, BookingCreateDto bookingCreateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("ADD-BOOKING Пользователь с id={} не найден", userId);
                    return new NotFoundException("Пользователя с id=" + userId + " не существует");
                });
        Item  item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> {
                    log.info("ADD-BOOKING Предмет с id={} не найден", bookingCreateDto.getItemId());
                    return new NotFoundException("Предмета с id=" + bookingCreateDto.getItemId() + " не существует");
                });

        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new IllegalArgumentException("Предмет с id=" + bookingCreateDto.getItemId() + " недоступен для бронирования");
        }

        Booking booking = BookingMapper.INSTANCE.toBooking(bookingCreateDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.INSTANCE.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto changeStatus(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.info("CHANGE-STATUS-BOOKING Аренда с id={} не найден", bookingId);
                    return new NotFoundException("Аренда с id=" + bookingId + " не существует");
                });
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Только владелец вещи может изменять статус бронирования");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IllegalArgumentException("Нельзя подтвердить бронь, которая уже подтверждена.");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.INSTANCE.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(long userId, long bookingId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("GET-BY-ID-BOOKING Пользователь с id={} не найден", userId);
                    return new NotFoundException("Пользователя с id=" + userId + " не существует");
                });
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.info("GET-BY-ID-BOOKING Аренда с id={} не найден", bookingId);
                    return new NotFoundException("Аренда с id=" + bookingId + " не существует");
                });
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Только автор бронирования или владелец вещи могут просматривать бронирование");
        }
        return BookingMapper.INSTANCE.toBookingDto(booking);
    }

//    @Override
//    public Collection<BookingDto> getByBookerId(long bookerId, BookingState state) {
//        userRepository.findById(bookerId)
//                .orElseThrow(() -> {
//                    log.info("GET-BY-BOOKER-ID Пользователь с id={} не найден", bookerId);
//                    return new NotFoundException("Пользователя с id=" + bookerId + " не существует");
//                });
//        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
//        Collection<Booking> bookings = switch (state) {
//            case CURRENT -> bookingRepository
//                    .findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(bookerId, now, now);
//            case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now);
//            case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, now);
//            case WAITING -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
//            case REJECTED -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
//            case ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
//            default -> throw new IllegalStateException("Unexpected value: " + state);
//        };
//        return bookings.stream().map(BookingMapper.INSTANCE::toBookingDto).collect(Collectors.toList());
//    }

//    @Override
//    public Collection<BookingDto> getByOwnerId(long ownerId, BookingState state) {
//        userRepository.findById(ownerId)
//                .orElseThrow(() -> {
//                    log.info("GET-BY-OWNER-ID Пользователь с id={} не найден", ownerId);
//                    return new NotFoundException("Пользователя с id=" + ownerId + " не существует");
//                });
//        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
//        Collection<Booking> bookings = switch (state) {
//            case CURRENT -> bookingRepository
//                    .findAllByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(ownerId, now, now);
//            case PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now);
//            case FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now);
//            case WAITING -> bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
//            case REJECTED -> bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
//            case ALL -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId);
//            default -> throw new IllegalStateException("Unexpected value: " + state);
//        };
//
//        return bookings.stream().map(BookingMapper.INSTANCE::toBookingDto).collect(Collectors.toList());
//    }
}
