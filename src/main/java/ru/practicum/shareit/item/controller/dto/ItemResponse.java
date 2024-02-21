package ru.practicum.shareit.item.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
@Builder
@AllArgsConstructor
public class ItemResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private ItemRequest request;
}
