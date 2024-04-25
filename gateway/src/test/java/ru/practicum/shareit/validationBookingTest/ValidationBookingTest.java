package ru.practicum.shareit.validationBookingTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.exceptions.ValidationException;
import ru.practicum.shareit.booking.validate.ValidateBooking;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ValidationBookingTest {

    @Autowired
    private ValidateBooking validateBooking;

    private BookingCreateRequest bookingCreateRequest;
    private LocalDateTime time = LocalDateTime.now();

    @Test
    public void validate_DataTimeNull() {

        bookingCreateRequest = new BookingCreateRequest(1L, null, time);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validateBooking.validate(bookingCreateRequest));

        assertEquals("Отсутствует одна из дат (начало/Окончание)", exception.getMessage());
    }

    @Test
    public void validate_StartAfterEnd() {

        bookingCreateRequest = new BookingCreateRequest(1L, time, time.minusMinutes(1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validateBooking.validate(bookingCreateRequest));

        assertEquals("Дата окончания брони раньше даты начала", exception.getMessage());
    }

    @Test
    public void validate_StartEquallyEnd() {

        bookingCreateRequest = new BookingCreateRequest(1L, time, time);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validateBooking.validate(bookingCreateRequest));

        assertEquals("Дата окончания брони та же что и начала", exception.getMessage());
    }

    @Test
    public void validateTest_Positive() {

        bookingCreateRequest = new BookingCreateRequest(1L, time.plusMinutes(1), time.plusMinutes(2));

        Boolean isValid = validateBooking.validate(bookingCreateRequest);

        assertEquals(true, isValid);
    }
}
