package ru.practicum.shareit.booking.exceptions;

public class ResourceServerError extends RuntimeException {
    public ResourceServerError(String message) {
        super(message);
    }
}
