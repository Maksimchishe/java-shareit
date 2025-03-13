package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.*;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentGetDto commentToGetDto(Comment comment);

    Comment commentToCreateDto(CommentCreateDto commentCreateDto);
}
