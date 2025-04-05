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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    private BookingCreateDto bookingCreateDto;
    private BookingGetDto bookingGetDto1;
    private BookingGetDto bookingGetDto2;
    private Item item1;
    private Item item2;
    private User user1;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "name1", "email1@email.email", null);
        item1 = new Item(1L, "name1", "description1", true, 1L, 1L, null);
        item2 = new Item(2L, "name2", "description2", true, 2L, 2L, null);
        bookingCreateDto = new BookingCreateDto(1L, null, null);
        bookingGetDto1 = new BookingGetDto(1L, null, null, 1L, item1, user1, BookingState.WAITING);
        bookingGetDto2 = new BookingGetDto(2L, null, null, 2L, item2, user1, BookingState.WAITING);

    }

    @Test
    void testCreateBooking() throws Exception {
        String bookingDTOJson = objectMapper.writeValueAsString(bookingCreateDto);

        when(bookingService.saveBooking(bookingCreateDto, 1L))
                .thenReturn(bookingGetDto1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingDTOJson))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDTOJson));
    }

    @Test
    void testChangeBookingStatus() throws Exception {
        String bookingDTOJson = objectMapper.writeValueAsString(bookingGetDto1);

        when(bookingService.approvedBooking(2L, true, 1L))
                .thenReturn(bookingGetDto1);

        mvc.perform(patch("/bookings/" + 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDTOJson));
    }

    @Test
    void testGetBookingByBooker() throws Exception {
        String bookingDTOJson = objectMapper.writeValueAsString(bookingGetDto1);

        when(bookingService.getBookingByBookerId(1L, 1L))
                .thenReturn(bookingGetDto1);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(bookingDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDTOJson));
    }

    @Test
    void testEmptyBookingsByOwner() throws Exception {
        when(bookingService.getAllByOwnerId(anyLong(), any()))
                .thenReturn(List.of());

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAllBookingsByOwner() throws Exception {
        List<BookingGetDto> testList = new ArrayList<>(List.of(bookingGetDto1, bookingGetDto2));

        String bookingDTOJson = objectMapper.writeValueAsString(testList);

        when(bookingService.getAllByOwnerId(anyLong(), any()))
                .thenReturn(testList);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(bookingDTOJson));
    }
}