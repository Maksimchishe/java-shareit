package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

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
    private ItemService itemService;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;

    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private ItemGetDto itemGetDto1;
    private ItemGetDto itemGetDto2;
    private CommentCreateDto commentCreateDto;
    private CommentGetDto commentGetDto;
    private Item item;
    private User user;

    @BeforeEach
    void beforeEach() {
        item = new Item(1L, "name", "description", true, 1L, 1L, null);
        user = new User(1L, "name", "mail@mail.com", null);
        itemCreateDto = new ItemCreateDto("name1", "description1", true, 1L, 1L);
        itemUpdateDto = new ItemUpdateDto("name1", "description1", true, 1L, 1L);
        itemGetDto1 = new ItemGetDto(1L, "name1", "description1", true, 1L, 1L, List.of(), null, null);
        itemGetDto2 = new ItemGetDto(2L, "name2", "description2", true, 2L, 2L, List.of(), null, null);
        commentCreateDto = new CommentCreateDto("comment");
        commentGetDto = new CommentGetDto(1L, "comment", "autor", null);
    }

    @Test
    void testCreateItem() throws Exception {
        String itemDTOJson = objectMapper.writeValueAsString(itemGetDto1);

        when(itemService.saveItem(any(ItemCreateDto.class), anyLong()))
                .thenReturn(itemGetDto1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemDTOJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDTOJson));
    }

    @Test
    void testCreateItemAndWithoutHeader() throws Exception {
        String itemDTOJson = objectMapper.writeValueAsString(itemGetDto1);

        when(itemService.saveItem(any(ItemCreateDto.class), anyLong()))
                .thenReturn(itemGetDto1);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemDTOJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testItemById() throws Exception {
        String itemDTOJson = objectMapper.writeValueAsString(itemGetDto1);

        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemGetDto1);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDTOJson));
    }

    @Test
    void testSearchText() throws Exception {
        when(itemService.search(anyString()))
                .thenReturn(List.of(itemGetDto1));

        mvc.perform(get("/items/search").queryParam("text", "Second Item")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemGetDto1))));
    }

    @Test
    void testEmptyItems() throws Exception {
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testUpdateItem() throws Exception {
        String itemDTOJson = objectMapper.writeValueAsString(itemGetDto1);

        when(itemService.updateItem(any(ItemUpdateDto.class), anyLong(), anyLong()))
                .thenReturn(itemGetDto1);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(itemDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDTOJson));
    }

    @Test
    void testDeleteItemById() throws Exception {
        itemService.deleteById(item.getId(), user.getId());

        mvc.perform(delete("/items/1", user.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateComment() throws Exception {
        String commentDTOJson = objectMapper.writeValueAsString(commentGetDto);

        when(itemService.saveComment(anyLong(), any(CommentCreateDto.class), anyLong()))
                .thenReturn(commentGetDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentDTOJson))
                .andExpect(status().isOk())
                .andExpect(content().json(commentDTOJson));
    }
}
