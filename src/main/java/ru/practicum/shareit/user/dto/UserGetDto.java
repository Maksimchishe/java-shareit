package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserGetDto {
    private Long id;
    private String name;
    private String email;
}
