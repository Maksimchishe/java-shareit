package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {
    private final UserService userService;

    @Test
    void testAllUsers() {
        UserCreateDto userCreateDto1 = new UserCreateDto("name1", "email1@email.email");
        UserCreateDto userCreateDto2 = new UserCreateDto("name2", "email2@email.email");
        UserUpdateDto userUpdateDto1 = new UserUpdateDto("nameUpdate1", "emailUpdate1@email.email");
        UserUpdateDto userUpdateDto2 = new UserUpdateDto("nameUpdate2", "emailUpdate2@email.email");

        UserGetDto userGetDto1 = userService.saveUser(userCreateDto1);
        UserGetDto userGetDto2 = userService.saveUser(userCreateDto2);
        Assertions.assertEquals(1L, userGetDto1.getId());
        Assertions.assertEquals(2L, userGetDto2.getId());

        userUpdateDto1.setName(null);
        UserGetDto userGetDto3 = userService.updateUser(userUpdateDto1, 1L);
        Assertions.assertEquals(1L, userGetDto3.getId());
        Assertions.assertEquals("name1", userGetDto3.getName());
        Assertions.assertEquals("emailUpdate1@email.email", userGetDto3.getEmail());

        userUpdateDto1.setName("name1");
        userUpdateDto1.setEmail(null);
        UserGetDto userGetDto4 = userService.updateUser(userUpdateDto1, 1L);
        Assertions.assertEquals(1L, userGetDto4.getId());
        Assertions.assertEquals("name1", userGetDto4.getName());
        Assertions.assertEquals("emailUpdate1@email.email", userGetDto3.getEmail());

        UserGetDto userGetDto5 = userService.updateUser(userUpdateDto1, 1L);
        UserGetDto userGetDto6 = userService.updateUser(userUpdateDto2, 2L);
        Assertions.assertEquals(1L, userGetDto5.getId());
        Assertions.assertEquals(2L, userGetDto6.getId());

        userUpdateDto1.setName(null);
        userUpdateDto1.setEmail(null);
        final ValidationException exceptionValidUpdate = assertThrows(ValidationException.class,
                () -> userService.updateUser(userUpdateDto1, 1L));
        Assertions.assertEquals("Отсутствуют данные для обновления.", exceptionValidUpdate.getMessage());

        final NotFoundException exceptionUpdate = assertThrows(NotFoundException.class,
                () -> userService.updateUser(userUpdateDto1, 99999L));
        Assertions.assertEquals("User не найден.", exceptionUpdate.getMessage());

        List<UserGetDto> userGetDtos1 = userService.getAllUsers();
        Assertions.assertEquals(2, userGetDtos1.size());

        userService.deleteById(2L);
        List<UserGetDto> userGetDtos2 = userService.getAllUsers();
        Assertions.assertEquals(1, userGetDtos2.size());

        final NotFoundException exceptionDelete = assertThrows(NotFoundException.class,
                () -> userService.deleteById(99999L));
        Assertions.assertEquals("User не найден.", exceptionDelete.getMessage());

        UserGetDto userGetDto7 = userService.getUserById(1L);
        Assertions.assertEquals(1L, userGetDto7.getId());
    }
}
