package ru.practicum.shareit.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.user.controller.dto.UserResponse;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingResponse {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemResponse item;

    private UserResponse booker;

    private String status;
}
