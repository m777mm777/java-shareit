package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.constants.Constants;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.controller.Create;
import ru.practicum.shareit.user.controller.Update;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemResponse create(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId,
                               @Validated(Create.class) @RequestBody ItemCreateRequest request) {
        Item item = itemMapper.toItem(request);
        Item modified = itemService.createItem(userOwnerId, item);
        log.info("Create userOwnerId {} Item {}", userOwnerId, request);
        return itemMapper.toResponse(modified);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse update(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId,
                               @Validated(Update.class) @RequestBody ItemCreateRequest request,
                               @PathVariable Long itemId) {
        Item item = itemMapper.toItem(request);
        Item modified = itemService.update(userOwnerId, itemId, item);
        log.info("Update userOwnerId {} Item {}", userOwnerId, request);
        return itemMapper.toResponse(modified);
    }

    @GetMapping("/{itemId}")
    public ItemResponse findById(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId,
                                 @PathVariable Long itemId) {
        ItemResponse itemResponse = itemService.findById(itemId, userOwnerId);
        log.info("findById userOwnerId {} Item {}", userOwnerId, itemId);
        return itemResponse;
    }

    @GetMapping()
    public List<ItemResponse> getAll(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId) {
        List<ItemResponse> items = itemService.getAllItemsByOwner(userOwnerId);
        log.info("getAll userOwnerId {}", userOwnerId);
        return items;
    }

    @GetMapping("/search")
    public List<ItemResponse> searchItem(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId,
                                         @RequestParam String text) {
        List<Item> items = itemService.searchItem(text);
        log.info("searchItem userOwnerId {} text {}", userOwnerId, text);
        return itemMapper.toResponseCollection(items);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse createComment(@RequestHeader(Constants.RESPONSEHEADER) Long authorId,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestBody @Valid CommentCreateRequest request) {
        log.info("CreateComment authorId {} itemId {} request {}", authorId, itemId, request);
        return itemService.createComment(authorId, itemId, request);
    }
}