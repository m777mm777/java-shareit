package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.createAndUpdate.Create;
import ru.practicum.shareit.createAndUpdate.Update;
import ru.practicum.shareit.user.dto.UserCreateRequest;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody UserCreateRequest request) {
        log.info("create User {}", request);
        return userClient.create(request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody UserCreateRequest request, @PathVariable Long id) {
        log.info("update User {} id {}", request, id);
        return userClient.update(request, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        log.info("findById id {}", id);
        return userClient.findById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("getAll ");
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeById(@PathVariable Long id) {
        log.info("removeById id {}", id);
        return userClient.removeById(id);
    }
}
