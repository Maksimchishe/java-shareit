package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class UserDto {
    @NonNull
    Long id;
    @NotBlank(message = "Имя или логин пользователя не может быть пустым.")
    String name;
    @NotBlank(message = "email не может быть пустым.")
    @Email(regexp = "^[\\w-.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$", message = "Некорректный формат email.")
    String email;
}
