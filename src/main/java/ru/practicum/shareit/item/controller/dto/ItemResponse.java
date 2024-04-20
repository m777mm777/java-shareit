package ru.practicum.shareit.item.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.controller.dto.InformationBooking;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private InformationBooking lastBooking;

    private InformationBooking nextBooking;

    private List<CommentResponse> comments;

    private Long requestId;
}
