package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.Collection;

@Data
@Builder
public class ItemWithBookingsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Collection<CommentDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    public record BookingDto(Long id, Long bookerId) {}
}
