package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserClient client;

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserGetDto userGetDto1;
    private UserGetDto userGetDto2;

    @BeforeEach
    void beforeEach() {
        userCreateDto = new UserCreateDto("name1", "mail1@mail.mail");
        userUpdateDto = new UserUpdateDto("name1", "mail1@mail.mail");
        userGetDto1 = new UserGetDto(1L, "name1", "mail1@mail.mail");
        userGetDto2 = new UserGetDto(2L, "name2", "mail2@mail.mail");
    }

    @Test
    void testGetAllUsers() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of(userGetDto1, userGetDto2));

        when(client.getAllUsers())
                .thenReturn(response);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("name1", "name2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].email", containsInAnyOrder("mail1@mail.mail", "mail2@mail.mail")));
    }

    @Test
    void testEmptyUsers() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of());

        when(client.getAllUsers())
                .thenReturn(response);

        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetUserById() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(userGetDto1);

        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(client.getUserById(1L))
                .thenReturn(response);

        mvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testGetUserByFakeId() throws Exception {
        Mockito.doThrow(new NotFoundException("User not found"))
                .when(client).getUserById(99999L);

        mvc.perform(get("/users/99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(userGetDto1);

        String userDTOJson = objectMapper.writeValueAsString(userCreateDto);

        when(client.saveUser(any(UserCreateDto.class)))
                .thenReturn(response);

        mvc.perform(post("/users")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateUser() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(userGetDto1);

        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(client.updateUser(1L, userUpdateDto))
                .thenReturn(response);

        mvc.perform(patch("/users/1")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateOnlyNameUser() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(userGetDto1);

        userUpdateDto.setEmail(null);
        userGetDto1.setEmail(null);
        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(client.updateUser(1L, userUpdateDto))
                .thenReturn(response);

        mvc.perform(patch("/users/1")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateOnlyEmailUser() throws Exception {
        userUpdateDto.setName(null);
        userGetDto1.setName(null);

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(userGetDto1);

        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(client.updateUser(1L, userUpdateDto))
                .thenReturn(response);

        mvc.perform(patch("/users/1")
                        .content(userDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJson));
    }

    @Test
    void testUpdateNotNameAndEmailUser() throws Exception {
        userUpdateDto.setName(null);
        userUpdateDto.setEmail(null);
        userGetDto1.setName(null);
        userGetDto1.setEmail(null);

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(userGetDto1);

        String userDTOJson = objectMapper.writeValueAsString(userGetDto1);

        when(client.updateUser(1L, userUpdateDto))
                .thenReturn(response);

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
        Mockito.doThrow(new NotFoundException("User not found"))
                .when(client).deleteById(99999L);

        mvc.perform(delete("/users/" + 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}