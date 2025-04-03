package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserGetDto> getAllUsers() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .map(userMapper::userToGetDto)
                .toList();
    }

    @Override
    public UserGetDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User не найден."));
        return userMapper.userToGetDto(user);
    }

    @Override
    @Transactional
    public UserGetDto saveUser(UserCreateDto userCreateDto) {
        return userMapper.userToGetDto(
                userRepository.save(userMapper.createDtoToUser(userCreateDto)));
    }

    @Override
    @Transactional
    public UserGetDto updateUser(UserUpdateDto userUpdateDto, long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User не найден."));
        if (userUpdateDto.getName() ==null && userUpdateDto.getEmail() == null) {
            throw new ValidationException("Отсутствуют данные для обновления.");
        }
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        return userMapper.userToGetDto(userRepository.save(user));
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}