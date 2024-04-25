package ru.practicum.shareit.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String text;
    private ItemResponse itemResponse;
    private String authorName;
    private LocalDateTime created;
}
