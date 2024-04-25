package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(User user, Long id);

    User findById(Long id);

    List<User> getAll();

    Long removeById(Long id);
}
