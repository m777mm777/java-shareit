package ru.practicum.shareit.question.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.controller.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
public class QuestionCreateRequest {

    @Size(max = 255, groups = {Create.class})
    @NotBlank(groups = {Create.class,})
    private String description;
}
