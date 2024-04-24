package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.createAndUpdate.Create;
import ru.practicum.shareit.createAndUpdate.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UserCreateRequest {

    @Size(max = 255, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private String name;

    @Size(max = 255, groups = {Create.class, Update.class})
    @NotEmpty(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;
}
