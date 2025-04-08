package ru.practicum.shareit.user.model;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserGetDto userToGetDto(User user);

    User createDtoToUser(UserCreateDto userCreateDto);
}
