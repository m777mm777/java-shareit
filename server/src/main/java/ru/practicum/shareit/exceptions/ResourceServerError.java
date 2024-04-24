package ru.practicum.shareit.exceptions;

public class ResourceServerError extends RuntimeException {
    public ResourceServerError(String message) {
        super(message);
    }
}
