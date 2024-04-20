package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemService {

    public Item createItem(Long userOwnerId, ItemCreateRequest item);

    public Item update(Long userOwnerId, Long itemId, Item item);

    public ItemResponse findById(Long itemId, Long ownerId);

    public Item getItemById(Long itemId);

    public List<ItemResponse> getAllItemsByOwner(Long userOwnerId, Integer from, Integer size);

    public List<Item> searchItem(Long userId, String text, Integer from, Integer size);

    public CommentResponse createComment(Long authorId, Long itemId, CommentCreateRequest comment, LocalDateTime now);
}
