package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserGetDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserGetDto getReferenceById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserGetDto saveUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.saveUser(userCreateDto);
    }

    @PatchMapping("/{id}")
    public UserGetDto updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable Long id) {
        return userService.updateUser(userUpdateDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}