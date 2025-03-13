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

    @Override
    public List<UserGetDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper.INSTANCE::userToGetDto)
                .sorted(Comparator.comparing(UserGetDto::getId))
                .toList();
    }

    @Override
    public UserGetDto getUserById(long id) {
        return UserMapper.INSTANCE.userToGetDto(repository.getUserById(id));
    }

    @Override
    @Transactional
    public UserGetDto saveUser(UserCreateDto userCreateDto) {
        return UserMapper.INSTANCE.userToGetDto(
                repository.save(UserMapper.INSTANCE.createDtoToUser(userCreateDto)));
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
        return UserMapper.INSTANCE.userToGetDto(
                repository.save(user));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}