package ru.practicum.shareit.booking.validate;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.exceptions.ValidationException;


@Component
public class ValidateBooking {
    public boolean validate(BookingCreateRequest request) {

        if (request.getStart() == null || request.getEnd() == null) {
            throw new ValidationException("Отсутствует одна из дат (начало/Окончание)");
        }

        if (request.getStart().isAfter(request.getEnd())) {
            throw new ValidationException("Дата окончания брони раньше даты начала");
        }

        if (request.getStart().equals(request.getEnd())) {
            throw new ValidationException("Дата окончания брони та же что и начала");
        }

        return true;
    }
}
