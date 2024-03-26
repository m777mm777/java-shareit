package ru.practicum.shareit.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingCreateRequest {

    private Long itemId;

    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    private LocalDateTime end;
}
