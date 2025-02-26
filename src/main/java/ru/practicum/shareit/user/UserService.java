package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto getUserById(long id) {
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userStorage.createUser(UserMapper.createDtoToUser(userDto)));
    }

    public UserDto updateUser(UserUpdateDto userUpdateDto, long id) {
        return UserMapper.toUserDto(userStorage.updateUser(UserMapper.updateDtoToUser(userUpdateDto), id));
    }

    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }

}
