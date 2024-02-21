package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    public User create(User user);

    public User update(User user);

    public User findById(Long id);

    public List<User> getAll();

    public Long removeById(Long id);
}
