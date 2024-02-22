package ru.practicum.shareit.user.storage.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDbStorage implements UserStorage {

    private Map<Long, User> storage = new HashMap<>();

    private Long id = 0L;

    @Override
    public User create(User user) {

            checkEmail(user);
            User modified = generatedId(user);

            storage.put(modified.getId(), modified);
            return modified;

    }

    @Override
    public User update(User user) {
        checkEmail(user);
        toInclude(user);
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long id) {
        if (!storage.containsKey(id)) {
            throw new ResourceNotFoundException("Такого пользователя нет");
        }
        return storage.get(id);
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<User>(storage.values());
        return users;
    }

    @Override
    public Long removeById(Long id) {
        User user = storage.remove(id);
        return user.getId();
    }

    private User generatedId(User user) {
        user.setId(++id);
        return user;
    }

    private void checkEmail(User user) {
        if (user.getEmail() != null) {
            for (User userBeingChecked : storage.values()) {
                if ((user.getEmail().equals(userBeingChecked.getEmail())) && (!user.getId().equals(userBeingChecked.getId()))) {
                    throw new ErrorResponse("Такой email уже есть");
                }
            }
        }
    }

    private User toInclude(User user) {
        User oldUser = storage.get(user.getId());

        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }

        return user;
    }

}