package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentCreateDto {
    @NotBlank(message = "Comment text cannot be empty")
    @Size(max = 150, message = "Text of the comment must not exceed 150 characters")
    private String text;
}
