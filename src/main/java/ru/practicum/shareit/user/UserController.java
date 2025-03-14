package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserGetDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserGetDto getReferenceById(@PathVariable(name = "id") long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserGetDto saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.saveUser(userCreateDto);
    }

    @PatchMapping("/{id}")
    public UserGetDto updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto, @PathVariable @Valid @NonNull Long id) {
        return userService.updateUser(userUpdateDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Valid @NonNull Long id) {
        userService.deleteById(id);
    }
}