package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String name;
    private String email;
}
