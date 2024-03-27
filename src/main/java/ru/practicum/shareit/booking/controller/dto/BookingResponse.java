package ru.practicum.shareit.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingResponse {
    Long id;

    LocalDateTime start;

    LocalDateTime end;

    Item item;

    User booker;

    String status;
}
