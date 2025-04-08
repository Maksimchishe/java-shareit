package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    private String name;
    private String email;
}
