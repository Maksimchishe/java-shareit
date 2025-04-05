package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    private static final long FAKE_ID = 99999L;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    private User user1;
    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserGetDto userGetDto1;
    private UserGetDto userGetDto2;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "name1", "mail1@mail.mail", null);
        userCreateDto = new UserCreateDto("name1", "mail1@mail.mail");
        userUpdateDto = new UserUpdateDto("name1", "mail1@mail.mail");
        userGetDto1 = new UserGetDto(1L, "name1", "mail1@mail.mail");
        userGetDto2 = new UserGetDto(2L, "name2", "mail2@mail.mail");
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(userGetDto1, userGetDto2));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("name1", "name2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].email", containsInAnyOrder("mail1@mail.mail", "mail2@mail.mail")));
    }

    @Test
    void testEmptyUsers() throws Exception {
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetUserById() throws Exception {
        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(userService.getUserById(1L))
                .thenReturn(userGetDto1);

        mvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testGetUserByFakeId() throws Exception {
        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(userService.getUserById(FAKE_ID))
                .thenThrow(new NotFoundException("User не найден."));

        mvc.perform(get("/users/99999")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        String userDTOJson = objectMapper.writeValueAsString(userCreateDto);

        when(userService.saveUser(any(UserCreateDto.class)))
                .thenReturn(userGetDto1);

        mvc.perform(post("/users")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateUser() throws Exception {
        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(userService.updateUser(userUpdateDto, user1.getId()))
                .thenReturn(userGetDto1);

        mvc.perform(patch("/users/1")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateOnlyNameUser() throws Exception {
        userUpdateDto.setEmail("");
        userGetDto1.setEmail("");
        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(userService.updateUser(userUpdateDto, user1.getId()))
                .thenReturn(userGetDto1);

        mvc.perform(patch("/users/1")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateOnlyEmailUser() throws Exception {
        userUpdateDto.setName("");
        userGetDto1.setName("");
        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(userService.updateUser(userUpdateDto, user1.getId()))
                .thenReturn(userGetDto1);

        mvc.perform(patch("/users/1")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateNotNameAndEmailUser() throws Exception {
        userUpdateDto.setName("");
        userUpdateDto.setEmail("");
        userGetDto1.setName("");
        userGetDto1.setEmail("");
        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(userService.updateUser(userUpdateDto, user1.getId()))
                .thenThrow(new ValidationException("Отсутствуют данные для обновления."));

        mvc.perform(patch("/users/1")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUserById() throws Exception {
        mvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteFakeId() throws Exception {
        Mockito.doThrow(new NotFoundException("User не найден."))
                .when(userService).deleteById(FAKE_ID);

        mvc.perform(delete("/users/" + FAKE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}