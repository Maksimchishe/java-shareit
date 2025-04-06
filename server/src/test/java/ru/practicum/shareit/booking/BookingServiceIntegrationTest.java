package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void testAllBookings() {
        UserCreateDto userCreateDto1 = new UserCreateDto("name1", "email1@email.email");
        UserCreateDto userCreateDto2 = new UserCreateDto("name2", "email2@email.email");
        BookingCreateDto bookingCreateDto1 = new BookingCreateDto(1L, null, null);
        BookingCreateDto bookingCreateDto2 = new BookingCreateDto(2L, null, null);
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("name", "description", true, 1L, 1L);
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("name", "description", false, 1L, 1L);

        userService.saveUser(userCreateDto1);
        List<BookingGetDto> bookingGetDtos = bookingService.findAll();
        Assertions.assertEquals(0, bookingGetDtos.size());

        itemService.saveItem(itemCreateDto1, 1L);
        BookingGetDto bookingGetDto = bookingService.saveBooking(bookingCreateDto1, 1L);
        Assertions.assertEquals(1L, bookingGetDto.getId());

        itemService.saveItem(itemCreateDto2, 1L);
        final ValidationException exceptionFalse = assertThrows(ValidationException.class,
                () -> bookingService.saveBooking(bookingCreateDto2, 1L));
        Assertions.assertEquals("isAvailable = false", exceptionFalse.getMessage());

        BookingGetDto bookingGetDto1 = bookingService.getBookingByBookerId(1L, 1L);
        Assertions.assertEquals(1L, bookingGetDto1.getId());

        userService.saveUser(userCreateDto2);
        final NotFoundException exceptionId = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingByBookerId(1L, 2L));
        Assertions.assertEquals("User не владелец и не заказчик Item", exceptionId.getMessage());

        List<BookingGetDto> bookingGetDtoALL = bookingService.getAllByOwnerId(1L, "ALL");
        Assertions.assertEquals(1, bookingGetDtoALL.size());

        List<BookingGetDto> bookingGetDtoCURRENT = bookingService.getAllByOwnerId(1L, "CURRENT");
        Assertions.assertEquals(0, bookingGetDtoCURRENT.size());

        List<BookingGetDto> bookingGetDtoPAST = bookingService.getAllByOwnerId(1L, "PAST");
        Assertions.assertEquals(0, bookingGetDtoPAST.size());

        List<BookingGetDto> bookingGetDtoFUTURE = bookingService.getAllByOwnerId(1L, "FUTURE");
        Assertions.assertEquals(0, bookingGetDtoFUTURE.size());

        List<BookingGetDto> bookingGetDtoWAITING = bookingService.getAllByOwnerId(1L, "WAITING");
        Assertions.assertEquals(1, bookingGetDtoWAITING.size());

        List<BookingGetDto> bookingGetDtoREJECTED = bookingService.getAllByOwnerId(1L, "REJECTED");
        Assertions.assertEquals(0, bookingGetDtoREJECTED.size());

        final ValidationException bookingGetDtoNo = assertThrows(ValidationException.class,
                () -> bookingService.getAllByOwnerId(1L, "Неправильный статус."));
        Assertions.assertEquals("Неправильный статус.", bookingGetDtoNo.getMessage());

        final NotFoundException bookingGetDtoNotUser = assertThrows(NotFoundException.class,
                () -> bookingService.getAllByOwnerId(5L, "ALL"));
        Assertions.assertEquals("User не найден.", bookingGetDtoNotUser.getMessage());

        BookingGetDto bookingGetAPPROVED = bookingService.approvedBooking(1L, true, 1L);
        Assertions.assertEquals(BookingState.APPROVED, bookingGetAPPROVED.getStatus());

        final ValidationException bookingGetIsAPPROVED = assertThrows(ValidationException.class,
                () -> bookingService.approvedBooking(1L, true, 1L));
        Assertions.assertEquals("Бронирование уже одобрено.", bookingGetIsAPPROVED.getMessage());

        final ValidationException bookingGetUserNotUser = assertThrows(ValidationException.class,
                () -> bookingService.approvedBooking(1L, true, 2L));
        Assertions.assertEquals("User не владелец и не заказчик Item", bookingGetUserNotUser.getMessage());

        BookingGetDto bookingGetREJECTED = bookingService.approvedBooking(1L, false, 1L);
        Assertions.assertEquals(BookingState.REJECTED, bookingGetREJECTED.getStatus());

        bookingService.saveBooking(bookingCreateDto1, 2L);
        BookingGetDto bookingGetCANCELED = bookingService.approvedBooking(2L, false, 2L);
        Assertions.assertEquals(BookingState.CANCELED, bookingGetCANCELED.getStatus());
    }
}
