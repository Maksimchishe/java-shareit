package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc
class RequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RequestClient requestClient;

    private RequestCreateDto requestCreateDto;
    private RequestGetDto requestGetDto1;
    private RequestGetDto requestGetDto2;

    @BeforeEach
    void beforeEach() {
        requestCreateDto = new RequestCreateDto("description1", 1L, LocalDateTime.of(2025, 3, 1, 1, 1));
        requestGetDto1 = new RequestGetDto(1L, "description1", 1L, List.of(), LocalDateTime.of(2025, 3, 1, 1, 1));
        requestGetDto2 = new RequestGetDto(2L, "description2", 2L, List.of(), LocalDateTime.of(2025, 3, 1, 2, 1));
    }

    @Test
    void testCreateRequest() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(requestGetDto1);

        String requestDTOJson = objectMapper.writeValueAsString(requestCreateDto);

        when(requestClient.saveRequest(1L, requestCreateDto))
                .thenReturn(response);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(requestDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestDTOJson));
    }

    @Test
    void testCreateFakeId() throws Exception {
        String requestDTOJson = objectMapper.writeValueAsString(requestCreateDto);

        when(requestClient.saveRequest(99999L, requestCreateDto))
                .thenThrow(new NotFoundException("User not found"));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDTOJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEmptyRequestListByOwnerId() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of());

        when(requestClient.getRequestByOwnerId(1L))
                .thenReturn(response);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().json("[]"));
    }

    @Test
    void testRequestListByOwnerId() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of(requestGetDto1, requestGetDto2));

        when(requestClient.getRequestById(1L, 1L))
                .thenReturn(response);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].description",
                        containsInAnyOrder("description1", "description2")));
    }

    @Test
    void testRequestById() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(requestGetDto1);

        String requestDTOJson = objectMapper.writeValueAsString(requestCreateDto);

        when(requestClient.getRequestById(1L, 2L))
                .thenReturn(response);

        mvc.perform(get("/requests/" + 2)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestDTOJson));
    }
}