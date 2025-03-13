package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentCreateDto {
    @NotBlank
    private String text;
}