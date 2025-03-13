package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    private String name;
    @Email(regexp = "^[\\w-.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$", message = "Некорректный формат email.")
    private String email;
}
