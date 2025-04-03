package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentGetDto commentToGetDto(Comment comment);

    Comment commentToCreateDto(CommentCreateDto commentCreateDto);
}
