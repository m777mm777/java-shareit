package ru.practicum.shareit.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingCreateRequest {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
