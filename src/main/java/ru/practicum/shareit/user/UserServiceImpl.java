package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public List<UserGetDto> getAllUsers() {
        return repository.findAll().stream()
                .map(userMapper::userToGetDto)
                .sorted(Comparator.comparing(UserGetDto::getId))
                .toList();
    }

    @Override
    public UserGetDto getUserById(long id) {
        return userMapper.userToGetDto(repository.getUserById(id));
    }

    @Override
    @Transactional
    public UserGetDto saveUser(UserCreateDto userCreateDto) {
        return userMapper.userToGetDto(
                repository.save(userMapper.createDtoToUser(userCreateDto)));
    }

    @Override
    @Transactional
    public UserGetDto updateUser(UserUpdateDto userUpdateDto, long id) {
        User user = repository.getUserById(id);
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        return userMapper.userToGetDto(
                repository.save(user));
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}