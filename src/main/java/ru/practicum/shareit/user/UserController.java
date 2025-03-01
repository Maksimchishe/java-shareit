package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserGetDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserGetDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserGetDto createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @PatchMapping("/{id}")
    public UserGetDto updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto, @PathVariable @Valid @NonNull Long id) {
        return userService.updateUser(userUpdateDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(Long id) {
        if (id != null) {
            userService.deleteUser(id);
        }
    }
}
