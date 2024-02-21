package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    public User create(User user);

    public User update(User user, Long id);

    public User findById(Long id);

    public List<User> getAll();

    public Long removeById(Long id);
}
