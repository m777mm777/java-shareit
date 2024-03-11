package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.controller.dto.UserCreateRequest;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserResponse create(@Validated(Create.class) @RequestBody UserCreateRequest request) {
        User user = userMapper.toUser(request);
        User modified = userService.create(user);
        log.info("create User {}", request);
        return userMapper.toResponse(modified);
    }

    @PatchMapping("/{id}")
    public UserResponse update(@Validated(Update.class) @RequestBody UserCreateRequest request, @PathVariable Long id) {
        User user = userMapper.toUser(request);
        User modified = userService.update(user, id);
        log.info("update User {} id {}", request, id);
        return userMapper.toResponse(modified);
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        User modified = userService.findById(id);
        log.info("findById id {}", id);
        return userMapper.toResponse(modified);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        List<User> users = userService.getAll();
        log.info("getAll ");
        return userMapper.toResponseCollection(users);
    }

    @DeleteMapping("/{id}")
    public Long removeById(@PathVariable Long id) {
        log.info("removeById id {}", id);
        return userService.removeById(id);
    }
}
