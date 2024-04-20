package ru.practicum.shareit.question.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.user.controller.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class QuestionResponse {

    private Long id;

    private String description;

    private UserResponse creator;

    private LocalDateTime created;

    private List<ItemResponse> items;
}
