package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void testAllItems() {
        UserCreateDto userCreateDto1 = new UserCreateDto("name1", "email1@email.email");
        ItemCreateDto itemCreateDto = new ItemCreateDto("name", "description", true, 1L, 1L);
        ItemCreateDto itemCreateDtoNull = new ItemCreateDto("name", "description", true, 1L, null);
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto("name", "description", true, 1L, null);

        userService.saveUser(userCreateDto1);

        itemService.saveItem(itemCreateDto, 1L);
        itemService.saveItem(itemCreateDtoNull, 1L);
        List<ItemGetDto> items = itemService.findAll(1L);
        Assertions.assertEquals(2, items.size());

        final NotFoundException exceptionCreateNotUser = assertThrows(NotFoundException.class,
                () -> itemService.saveItem(itemCreateDto, 99999L));
        Assertions.assertEquals("User не найден.", exceptionCreateNotUser.getMessage());

        ItemGetDto itemGetDto0 = itemService.updateItem(itemUpdateDto, 1L, 1L);
        Assertions.assertEquals(3L, itemGetDto0.getId());

        final NotFoundException exceptionUpdateNotUser = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(itemUpdateDto, 1L, 99999L));
        Assertions.assertEquals("User не найден.", exceptionUpdateNotUser.getMessage());

        final NotFoundException exceptionUpdateNotItem = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(itemUpdateDto, 99999L, 1L));
        Assertions.assertEquals("Item не найден.", exceptionUpdateNotItem.getMessage());

        final NotFoundException exceptionCreate = assertThrows(NotFoundException.class,
                () -> itemService.findAll(99999L));
        Assertions.assertEquals("User не найден.", exceptionCreate.getMessage());

        ItemGetDto itemGetDto1 = itemService.saveItem(itemCreateDto, 1L);
        itemGetDto1.setOwner(1L);
        ItemGetDto itemGetDto2 = itemService.getItemById(1L, 1L);
        Assertions.assertEquals(1L, itemGetDto2.getId());

        final NotFoundException exceptionNotItem = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(99999L, 1L));
        Assertions.assertEquals("Item не найден.", exceptionNotItem.getMessage());

        final NotFoundException exceptionNotUser = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(1L, 99999L));
        Assertions.assertEquals("User не найден.", exceptionNotUser.getMessage());

        itemService.deleteById(1L, 1L);
        Assertions.assertEquals(2, items.size());

        final NotFoundException exceptionDeleteNotUser = assertThrows(NotFoundException.class,
                () -> itemService.deleteById(1L, 99999L));
        Assertions.assertEquals("User не найден.", exceptionDeleteNotUser.getMessage());

        final NotFoundException exceptionDeleteNotItem = assertThrows(NotFoundException.class,
                () -> itemService.deleteById(99999L, 1L));
        Assertions.assertEquals("Item не найден.", exceptionDeleteNotItem.getMessage());

        List<ItemGetDto> itemGetDtoSearch = itemService.search("test");
        Assertions.assertEquals(0, itemGetDtoSearch.size());

        List<ItemGetDto> itemGetDtoSearchEmpty = itemService.search("");
        Assertions.assertEquals(0, itemGetDtoSearchEmpty.size());

        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        final ValidationException exceptionComment = assertThrows(ValidationException.class,
                () -> itemService.saveComment(1L, commentCreateDto, 1L));
        Assertions.assertEquals("Нет бронирования.", exceptionComment.getMessage());

        BookingCreateDto bookingCreateDto = new BookingCreateDto(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        bookingService.saveBooking(bookingCreateDto, 1L);
        CommentGetDto commentGetDto = itemService.saveComment(1L, commentCreateDto, 1L);
        Assertions.assertEquals("test", commentGetDto.getText());
    }
}
