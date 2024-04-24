package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.createAndUpdate.Create;
import ru.practicum.shareit.createAndUpdate.Update;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId,
                                         @Validated(Create.class) @RequestBody ItemCreateRequest request) {
        log.info("Create userOwnerId {} Item {}", userOwnerId, request);
        return itemClient.create(userOwnerId, request);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId,
                               @Validated(Update.class) @RequestBody ItemCreateRequest request,
                               @PathVariable Long itemId) {
        log.info("Update userOwnerId {} Item {}", userOwnerId, request);
        return itemClient.update(userOwnerId, request, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@RequestHeader(Constants.RESPONSEHEADER) Long ownerId,
                                 @PathVariable Long itemId) {
        log.info("findById userOwnerId {} Item {}", ownerId, itemId);
        return itemClient.findById(itemId, ownerId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(Constants.RESPONSEHEADER) Long userOwnerId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("getAll userOwnerId {} from {} size {}", userOwnerId, from, size);
        return itemClient.getAllByOwner(userOwnerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(Constants.RESPONSEHEADER) Long userId,
                                         @RequestParam String text,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("searchItem userId {} text {} from {} size {}", userId, text, from, size);
        return itemClient.searchItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(Constants.RESPONSEHEADER) Long authorId,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestBody @Valid CommentCreateRequest request) {
        log.info("CreateComment authorId {} itemId {} request {}", authorId, itemId, request);
        return itemClient.createComment(authorId, itemId, request);
    }
}