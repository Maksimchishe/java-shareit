package ru.practicum.shareit.user.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserGetDto userToGetDto(User user);

    User createDtoToUser(UserCreateDto userCreateDto);
}
