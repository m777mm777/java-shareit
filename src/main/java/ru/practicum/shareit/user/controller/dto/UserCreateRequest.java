package ru.practicum.shareit.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.user.controller.Create;
import ru.practicum.shareit.user.controller.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(groups = {Create.class})
    private String name;

    @NotEmpty(groups = {Create.class})
    @Email(groups = {Create.class})
    @Email(groups = {Update.class})
    private String email;
}
