package ru.practicum.shareit.booking.validate;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.exceptions.ValidationException;

@Service
public class ValidateBooking {
    public void validate(BookingCreateRequest request) {

        if (request.getStart() == null || request.getEnd() == null) {
            throw new ValidationException("Отсутствует одна из дат (начало/Окончание)");
        }

        if (request.getStart().isAfter(request.getEnd())) {
            throw new ValidationException("Дата окончания брони раньше даты начала");
        }

        if (request.getStart().equals(request.getEnd())) {
            throw new ValidationException("Дата окончания брони та же что и начала");
        }
    }
}