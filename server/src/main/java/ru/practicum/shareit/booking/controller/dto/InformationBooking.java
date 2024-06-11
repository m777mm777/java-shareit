package ru.practicum.shareit.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.constants.StatusBooking;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformationBooking {

    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private StatusBooking status;
}