package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class CommentCreateRequest {

    @Size(max = 255)
    @NotBlank
    private String text;
}
