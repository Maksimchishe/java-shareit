package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserGetDto> getUsers() {
        return userRepository.getUsers().stream()
                .map(UserMapper.INSTANCE::userToGetDto)
                .toList();
    }

    public UserGetDto getUserById(long id) {
        return UserMapper.INSTANCE.userToGetDto(userRepository.getUserById(id));
    }

    public UserGetDto createUser(UserCreateDto userCreateDto) {
        return UserMapper.INSTANCE.userToGetDto(userRepository.createUser(UserMapper.INSTANCE.createDtoToUser(userCreateDto)));
    }

    public UserGetDto updateUser(UserUpdateDto userUpdateDto, long id) {
        return UserMapper.INSTANCE.userToGetDto(userRepository.updateUser(UserMapper.INSTANCE.updateDtoToUser(userUpdateDto), id));
    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

}
