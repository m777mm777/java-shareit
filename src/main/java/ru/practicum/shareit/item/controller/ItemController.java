package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.Itemservice;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final Itemservice itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemResponse create(@RequestHeader("X-Sharer-User-Id") Long userOwnerId,
                               @Valid @RequestBody ItemCreateRequest request) {
        Item item = itemMapper.toItem(request);
        Item modified = itemService.addNewItem(userOwnerId, item);
        log.info("Create userOwnerId {} Item {}", userOwnerId, request);
        return itemMapper.toResponse(modified);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse update(@RequestHeader("X-Sharer-User-Id") Long userOwnerId,
                               @RequestBody ItemCreateRequest request,
                               @PathVariable Long itemId) {
        Long u = itemId;
        Item item = itemMapper.toItem(request);
        Item modified = itemService.update(userOwnerId, itemId, item);
        log.info("Update userOwnerId {} Item {}", userOwnerId, request);
        return itemMapper.toResponse(modified);
    }

    @GetMapping("/{itemId}")
    public ItemResponse findById(@RequestHeader("X-Sharer-User-Id") Long userOwnerId,
                                 @PathVariable Long itemId) {
        Item item = itemService.findById(itemId);
        log.info("findById userOwnerId {} Item {}", userOwnerId, itemId);
        return itemMapper.toResponse(item);
    }

    @GetMapping()
    public List<ItemResponse> getAll(@RequestHeader("X-Sharer-User-Id") Long userOwnerId) {
        List<Item> items = itemService.getAll(userOwnerId);
        log.info("getAll userOwnerId {}", userOwnerId);
        return itemMapper.toResponseCollection(items);
    }

    @GetMapping("/search")
    public List<ItemResponse> searchItem(@RequestHeader("X-Sharer-User-Id") Long userOwnerId,
                                         @RequestParam(defaultValue = "null") String text) {
        List<Item> items = itemService.searchItem(text);
        log.info("searchItem userOwnerId {} text {}", userOwnerId, text);
        return itemMapper.toResponseCollection(items);
    }
}
