package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final UserService userService;

    @Test
    void testAllBookings() {
        UserCreateDto userCreateDto1 = new UserCreateDto("name1", "email1@email.email");

        userService.saveUser(userCreateDto1);
        List<BookingGetDto> bookingGetDtos = bookingService.findAll();
        Assertions.assertEquals(0, bookingGetDtos.size());
    }
}
