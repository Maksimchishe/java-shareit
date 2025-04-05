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
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemClient itemClient;

    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private ItemGetDto itemGetDto1;
    private ItemGetDto itemGetDto2;
    private CommentCreateDto commentCreateDto;
    private CommentGetDto commentGetDto;


    @BeforeEach
    void beforeEach() {
        itemCreateDto = new ItemCreateDto("name1", "description1", true, 1L, 1L);
        itemUpdateDto = new ItemUpdateDto("name1", "description1", true, 1L, 1L);
        itemGetDto1 = new ItemGetDto(1L, "name1", "description1", true, 1L, 1L, List.of(), null, null);
        itemGetDto2 = new ItemGetDto(2L, "name2", "description2", true, 2L, 2L, List.of(), null, null);
        commentCreateDto = new CommentCreateDto("comment");
        commentGetDto = new CommentGetDto(1L, "comment", "autor", null);
    }

    @Test
    void testCreateItem() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(itemGetDto1);

        String itemDTOJson = objectMapper.writeValueAsString(itemGetDto1);

        when(itemClient.saveItem(anyLong(), any(ItemCreateDto.class)))
                .thenReturn(response);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemDTOJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDTOJson));
    }

    @Test
    void testCreateItemAndWithoutHeader() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(itemGetDto1);

        String itemDTOJson = objectMapper.writeValueAsString(itemGetDto1);

        when(itemClient.saveItem(anyLong(), any(ItemCreateDto.class)))
                .thenReturn(response);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemDTOJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testItemById() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(itemGetDto1);

        String itemDTOJson = objectMapper.writeValueAsString(itemGetDto1);

        when(itemClient.getItemById(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDTOJson));
    }

    @Test
    void testSearchText() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of(itemGetDto1));

        when(itemClient.search(anyString()))
                .thenReturn(response);

        mvc.perform(get("/items/search").queryParam("text", "Second Item")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemGetDto1))));
    }

    @Test
    void testEmptyItems() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of());

        when(itemClient.findAll(1L))
                .thenReturn(response);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testDeleteItemById() throws Exception {
        mvc.perform(delete("/items/1", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateComment() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(commentGetDto);

        String commentDTOJson = objectMapper.writeValueAsString(commentGetDto);

        when(itemClient.saveComment(anyLong(), any(CommentCreateDto.class), anyLong()))
                .thenReturn(response);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentDTOJson))
                .andExpect(status().isOk())
                .andExpect(content().json(commentDTOJson));
    }
}
