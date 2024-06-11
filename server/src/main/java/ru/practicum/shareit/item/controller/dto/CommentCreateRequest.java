package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class CommentCreateRequest {
    private String text;
}
