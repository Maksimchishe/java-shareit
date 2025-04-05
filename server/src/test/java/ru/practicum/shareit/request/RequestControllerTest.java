package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;
import ru.practicum.shareit.request.model.RequestMapper;

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
    public static final long FAKE_ID = 99999L;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RequestService service;
    @MockBean
    private RequestMapper requestMapper;

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
        String requestDTOJson = objectMapper.writeValueAsString(requestGetDto1);

        when(service.saveRequest(requestCreateDto, 1L))
                .thenReturn(requestGetDto1);

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

        when(service.saveRequest(requestCreateDto, FAKE_ID))
                .thenThrow(new NotFoundException("User not found"));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", FAKE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDTOJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEmptyRequestListByOwnerId() throws Exception {
        when(service.getRequestByRequesterId(1L))
                .thenReturn(List.of());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().json("[]"));
    }

    @Test
    void testRequestListByOwnerId() throws Exception {
        when(service.getRequestByRequesterId(1L))
                .thenReturn(List.of(requestGetDto1, requestGetDto2));

        mvc.perform(get("/requests")
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
        String requestDTOJson = objectMapper.writeValueAsString(requestCreateDto);

        when(service.getRequestById(1L, 2L))
                .thenReturn(requestGetDto1);

        mvc.perform(get("/requests/" + 2)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestDTOJson));
    }
}