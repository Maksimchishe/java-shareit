package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    List<UserGetDto> getAllUsers();

    UserGetDto getUserById(long id);

    UserGetDto saveUser(UserCreateDto userCreateDto);

    UserGetDto updateUser(UserUpdateDto userUpdateDto, long id);

    void deleteById(long id);

}