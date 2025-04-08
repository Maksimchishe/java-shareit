package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceIntegrationTest {
    private final RequestService requestService;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void testAllRequest() {
        UserCreateDto userCreateDto1 = new UserCreateDto("name1", "email1@email.email");
        RequestGetDto requestGetDto;
        RequestCreateDto requestCreateDto = new RequestCreateDto("description", 1L, null);

        userService.saveUser(userCreateDto1);
        requestGetDto = requestService.saveRequest(requestCreateDto, 1L);
        Assertions.assertEquals(1L, requestGetDto.getId());

        final NotFoundException exceptionCreate = assertThrows(NotFoundException.class,
                () -> requestService.saveRequest(requestCreateDto, 99999L));
        Assertions.assertEquals("User не найден.", exceptionCreate.getMessage());

        requestGetDto = requestService.getRequestById(1L, 1L);
        Assertions.assertEquals(1L, requestGetDto.getId());

        final NotFoundException exceptionById = assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(99999L, 1L));
        Assertions.assertEquals("User не найден.", exceptionById.getMessage());

        List<RequestGetDto> requestsByRequesterId = requestService.getRequestByRequesterId(1L);
        Assertions.assertEquals(1, requestsByRequesterId.size());

        ItemCreateDto itemCreateDto = new ItemCreateDto("name", "description", true, 1L, 1L);
        itemService.saveItem(itemCreateDto, 1L);
        System.out.println(itemService.findAll(1L));
        final NotFoundException exceptionByRequesterId = assertThrows(NotFoundException.class,
                () -> requestService.getRequestByRequesterId(99999L));
        Assertions.assertEquals("User не найден.", exceptionByRequesterId.getMessage());
    }
}
