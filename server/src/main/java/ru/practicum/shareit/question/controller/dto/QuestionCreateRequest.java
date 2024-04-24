package ru.practicum.shareit.question.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class QuestionCreateRequest {

    private String description;
}
