package ru.practicum.shareit.exeptionTest;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {

    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    public void handleValidationExceptionTest() {
       final ValidationException exception = new ValidationException("Получен статус 400 Not found");
        ResponseEntity<ErrorMessage> message = errorHandler.handleValidationException(exception);
        assertEquals(message, message);
    }

    @Test
    public void handleNotFoundExceptionTest() {
        final ResourceNotFoundException exception = new ResourceNotFoundException("Получен статус 400 Not found");
        ResponseEntity<ErrorMessage> message = errorHandler.handleNotFoundException(exception);
        assertEquals(message, message);
    }

    @Test
    public void handleServerErrorTest() {
        final ResourceServerError exception = new ResourceServerError("Получен статус 400 Not found");
        ResponseEntity<ErrorMessage> message = errorHandler.handleServerError(exception);
        assertEquals(message, message);
    }

    @Test
    public void unknownExceptionTest() {
        final Throwable exception = new Throwable("Получен статус 400 Not found");
        ResponseEntity<ErrorMessage> message = errorHandler.unknownException(exception);
        assertEquals(message, message);
    }
}
