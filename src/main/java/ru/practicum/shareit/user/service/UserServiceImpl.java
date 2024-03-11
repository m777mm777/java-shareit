package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user, Long id) {
        findById(id);
        user.setId(id);
        return userStorage.update(user);
    }

    @Override
    public User findById(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public Long removeById(Long id) {
        findById(id);
        return userStorage.removeById(id);
    }
}
