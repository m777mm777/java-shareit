package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import ru.practicum.shareit.createAndUpdate.Create;
import ru.practicum.shareit.createAndUpdate.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class ItemCreateRequest {

    @Size(max = 255, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class,})
    private String name;

    @Size(max = 255, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

    private Long requestId;
}
