package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
public class ErrorResponse extends RuntimeException {
    public ErrorResponse(String message) {
        super(message);
    }
}