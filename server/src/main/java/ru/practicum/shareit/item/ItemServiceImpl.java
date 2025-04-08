package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
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
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    public List<ItemGetDto> findAll(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        return itemRepository.findAll().stream()
                .filter(i -> i.getOwner() == userId)
                .map(itemMapper::itemToGetDto)
                .sorted(Comparator.comparing(ItemGetDto::getId))
                .toList();
    }

    @Override
    public ItemGetDto getItemById(long id, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item не найден."));
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
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        Item item = itemMapper.itemToCreateDto(itemCreateDto);
        item.setOwner(userId);
        if (itemCreateDto.getRequestId() != null) {
            item.setRequest(itemCreateDto.getRequestId());
        } else {
            item.setRequest(0);
        }
        return itemMapper.itemToGetDto(
                itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemGetDto updateItem(ItemUpdateDto itemUpdateDto, long id, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Item не найден.");
        }
        return itemMapper.itemToGetDto(itemRepository.save(itemMapper.itemToUpdateDto(itemUpdateDto)));
    }

    @Override
    public void deleteById(long id, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User не найден.");
        }
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Item не найден.");
        }
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User не найден."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден."));
        List<Booking> bookings = bookingRepository.findAllByBookerAndItem(userId, itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("Нет бронирования.");
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