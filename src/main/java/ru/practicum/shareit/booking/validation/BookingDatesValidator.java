package ru.practicum.shareit.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

public class BookingDatesValidator implements ConstraintValidator<ValidBookingDates, BookingCreateDto> {

    @Override
    public boolean isValid(BookingCreateDto bookingCreateDto, ConstraintValidatorContext context) {
        if (bookingCreateDto == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();

        if (bookingCreateDto.getStart() == null || bookingCreateDto.getEnd() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Дата не может быть пустой")
                    .addConstraintViolation();
            return false;
        }

        if (bookingCreateDto.getStart().isBefore(now)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Время начала не должно быть в прошлом")
                    .addConstraintViolation();
            return false;
        }

        if (bookingCreateDto.getEnd().isBefore(now)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Время окончания не должно быть в прошлом")
                    .addConstraintViolation();
            return false;
        }

        if (!bookingCreateDto.getStart().isBefore(bookingCreateDto.getEnd())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Дата начала бронирования должна быть до даты окончания бронирования")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
