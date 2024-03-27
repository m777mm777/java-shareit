package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    public Item createItem(Long userOwnerId, Item item);

    public Item update(Long userOwnerId, Long itemId, Item item);

    public ItemResponse findById(Long itemId, Long ownerId);

    public Item getItemById(Long itemId);

    public List<ItemResponse> getAll(Long userOwnerId);

    public List<Item> searchItem(String text);

    public CommentResponse createComment(Long authorId, Long itemId, CommentCreateRequest comment);
}
