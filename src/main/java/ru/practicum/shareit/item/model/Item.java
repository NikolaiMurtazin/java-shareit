package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Item {
    private Long id;
    @NotBlank(message = "Item name can't be blank")
    private String name;
    @NotBlank(message = "Item description can't be blank")
    private String description;
    @NotNull(message = "Item availability must be specified")
    private Boolean available;
    @NotNull(message = "Item owner ID must be specified")
    private Long ownerId;
}
