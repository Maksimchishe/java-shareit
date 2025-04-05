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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;

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
    private BookingClient bookingClient;

    private BookingCreateDto bookingCreateDto;
    private BookingGetDto bookingGetDto1;
    private BookingGetDto bookingGetDto2;

    @BeforeEach
    void beforeEach() {
        bookingCreateDto = new BookingCreateDto(1L, null, null);
        bookingGetDto1 = new BookingGetDto(1L, null, null, 1L, BookingState.WAITING);
        bookingGetDto2 = new BookingGetDto(2L, null, null, 2L, BookingState.WAITING);
    }

    @Test
    void testCreateBooking() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(bookingGetDto1);

        String bookingDTOJson = objectMapper.writeValueAsString(bookingCreateDto);

        when(bookingClient.saveBooking(bookingCreateDto, 1L))
                .thenReturn(response);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingDTOJson))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDTOJson));
    }

    @Test
    void testChangeBookingStatus() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(bookingGetDto1);

        String bookingDTOJson = objectMapper.writeValueAsString(bookingGetDto1);

        when(bookingClient.approvedBooking(1L, 2L, true))
                .thenReturn(response);

        mvc.perform(patch("/bookings/" + 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDTOJson));
    }

    @Test
    void testGetBookingByBooker() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(bookingGetDto1);

        String bookingDTOJson = objectMapper.writeValueAsString(bookingGetDto1);

        when(bookingClient.getBookingById(1L, 1L))
                .thenReturn(response);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(bookingDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDTOJson));
    }

    @Test
    void testEmptyBookingsByOwner() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of());

        when(bookingClient.getBookingsByOwnerId(anyLong(), any()))
                .thenReturn(response);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAllBookingsByOwner() throws Exception {
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of(bookingGetDto1, bookingGetDto2));

        String bookingDTOJson = objectMapper.writeValueAsString(List.of(bookingGetDto1, bookingGetDto2));

        when(bookingClient.getBookingsByOwnerId(anyLong(), any()))
                .thenReturn(response);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(bookingDTOJson));
    }
}