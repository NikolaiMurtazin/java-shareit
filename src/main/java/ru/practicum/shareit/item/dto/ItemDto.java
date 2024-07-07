package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Item name can't be blank")
    @Size(max = 30, message = "Item name should be no more than 30 characters long")
    private String name;

    @NotBlank(message = "Item description can't be blank")
    @Size(max = 150, message = "Item description should be no more than 150 characters long")
    private String description;

    @NotNull(message = "Item availability must be specified")
    private Boolean available;
}
