package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Имя или логин пользователя не может быть пустым.")
    private String name;
    @NotBlank(message = "email не может быть пустым.")
    @Email(regexp = "^[\\w-.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$", message = "Некорректный формат email.")
    private String email;
}
