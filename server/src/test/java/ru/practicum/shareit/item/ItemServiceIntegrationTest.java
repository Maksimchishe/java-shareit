package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void testAllItems() {
        UserCreateDto userCreateDto1 = new UserCreateDto("name1", "email1@email.email");

        userService.saveUser(userCreateDto1);
        List<ItemGetDto> items = itemService.findAll(1L);
        Assertions.assertEquals(0, items.size());

        final NotFoundException exceptionCreate = assertThrows(NotFoundException.class,
                () -> itemService.findAll(99999L));
        Assertions.assertEquals("User не найден.", exceptionCreate.getMessage());
    }
}
