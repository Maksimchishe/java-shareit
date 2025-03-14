package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    public List<ItemGetDto> findAll(long userId) {
        userRepository.existsUserById(userId);
        return itemRepository.findAll().stream()
                .filter(i -> i.getOwner() == userId)
                .map(itemMapper::itemToGetDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .toList();
    }

    @Override
    public ItemGetDto getItemById(long id, long userId) {
        Item item = itemRepository.getItemById(id);
        ItemGetDto itemGetDto = itemMapper.itemToGetDto(item);

        if (item.getOwner() == userId) {
            setBooking(itemGetDto, id);
        }

        itemGetDto.setComments(commentRepository.findAllByItemId(id).stream()
                .map(commentMapper::commentToGetDto)
                .toList());
        return itemGetDto;
    }

    @Override
    @Transactional
    public ItemGetDto saveItem(ItemCreateDto itemCreateDto, long userId) {
        userRepository.existsUserById(userId);
        itemCreateDto.setOwner(userId);
        return itemMapper.itemToGetDto(
                itemRepository.save(itemMapper.itemToCreateDto(itemCreateDto)));
    }

    @Override
    @Transactional
    public ItemGetDto updateItem(ItemUpdateDto itemUpdateDto, long id, long userId) {
        userRepository.existsUserById(userId);
        itemRepository.existsItemById(id);
        return itemMapper.itemToGetDto(itemRepository.save(itemMapper.itemToUpdateDto(itemUpdateDto)));
    }

    @Override
    public void deleteById(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemGetDto> search(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(text).stream()
                .map(itemMapper::itemToGetDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .toList();
    }

    @Override
    @Transactional
    public CommentGetDto saveComment(long itemId, CommentCreateDto commentCreateDto, long userId) {
        User user = userRepository.getUserById(userId);
        Item item = itemRepository.getItemById(itemId);

        if (bookingRepository.findAllByBookerAndItem(userId, itemId, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Нет бронирования");
        } else {
            Comment comment = commentMapper.commentToCreateDto(commentCreateDto);
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(LocalDateTime.now());

            CommentGetDto commentGetDto = commentMapper.commentToGetDto(commentRepository.save(comment));
            commentGetDto.setAuthorName(user.getName());
            return commentGetDto;
        }
    }

    private void setBooking(ItemGetDto itemGetDto, long itemId) {
        Optional<Booking> lastBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());
        Optional<Booking> nextBooking = bookingRepository.findNextBooking(itemId, LocalDateTime.now());

        lastBooking.ifPresent(booking -> itemGetDto.setLastBooking(bookingMapper.bookingToSimpleDto(booking)));
        nextBooking.ifPresent(booking -> itemGetDto.setNextBooking(bookingMapper.bookingToSimpleDto(booking)));
    }
}