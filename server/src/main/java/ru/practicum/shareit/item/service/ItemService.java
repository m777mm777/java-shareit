package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemService {

    Item createItem(Long userOwnerId, ItemCreateRequest item);

    Item update(Long userOwnerId, Long itemId, Item item);

    ItemResponse findById(Long itemId, Long ownerId);

    Item getItemById(Long itemId);

    List<ItemResponse> getAllItemsByOwner(Long userOwnerId, Integer from, Integer size);

    List<Item> searchItem(Long userId, String text, Integer from, Integer size);

    CommentResponse createComment(Long authorId, Long itemId, CommentCreateRequest comment, LocalDateTime now);
}
