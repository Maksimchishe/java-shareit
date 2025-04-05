package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getReferenceById(@PathVariable(name = "id") long id) {
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userClient.saveUser(userCreateDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable @Valid @NonNull Long id, @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return userClient.updateUser(id, userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Valid @NonNull Long id) {
        userClient.deleteById(id);
    }
}