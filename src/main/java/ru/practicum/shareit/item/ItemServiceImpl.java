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

    @Override
    public List<ItemGetDto> findAll(long userId) {
        userRepository.getUserById(userId);
        return itemRepository.findAll().stream()
                .filter(i -> i.getOwner() == userId)
                .map(ItemMapper.INSTANCE::itemToGetDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .toList();
    }

    @Override
    public ItemGetDto getItemById(long id, long userId) {
        Item item = itemRepository.getItemById(id);
        ItemGetDto itemGetDto = ItemMapper.INSTANCE.itemToGetDto(item);

        if (item.getOwner() == userId) {
            setBooking(itemGetDto, id);
        }

        itemGetDto.setComments(commentRepository.findAllByItemId(id).stream()
                .map(CommentMapper.INSTANCE::commentToGetDto)
                .toList());
        return itemGetDto;
    }

    @Override
    @Transactional
    public ItemGetDto saveItem(ItemCreateDto itemCreateDto, long userId) {
        userRepository.getUserById(userId);
        itemCreateDto.setOwner(userId);
        return ItemMapper.INSTANCE.itemToGetDto(
                itemRepository.save(ItemMapper.INSTANCE.itemToCreateDto(itemCreateDto)));
    }

    @Override
    @Transactional
    public ItemGetDto updateItem(ItemUpdateDto itemUpdateDto, long id, long userId) {
        userRepository.getUserById(userId);
        itemRepository.getItemById(id);
        return ItemMapper.INSTANCE.itemToGetDto(itemRepository.save(ItemMapper.INSTANCE.itemToUpdateDto(itemUpdateDto)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemGetDto> search(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper.INSTANCE::itemToGetDto)
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
            Comment comment = CommentMapper.INSTANCE.commentToCreateDto(commentCreateDto);
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(LocalDateTime.now());

            CommentGetDto commentGetDto = CommentMapper.INSTANCE.commentToGetDto(commentRepository.save(comment));
            commentGetDto.setAuthorName(user.getName());
            return commentGetDto;
        }
    }

    private void setBooking(ItemGetDto itemGetDto, long itemId) {
        Optional<Booking> lastBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());
        Optional<Booking> nextBooking = bookingRepository.findNextBooking(itemId, LocalDateTime.now());

        lastBooking.ifPresent(booking -> itemGetDto.setLastBooking(BookingMapper.INSTANCE.bookingToSimpleDto(booking)));
        nextBooking.ifPresent(booking -> itemGetDto.setNextBooking(BookingMapper.INSTANCE.bookingToSimpleDto(booking)));
    }
}