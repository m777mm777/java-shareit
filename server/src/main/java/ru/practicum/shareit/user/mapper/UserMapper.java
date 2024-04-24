package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.controller.dto.UserCreateRequest;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserMapper() {
    }

    public User toUser(UserCreateRequest request) {
        if (request == null) {
            return null;
        }

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            return user;
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

            UserResponse.UserResponseBuilder userResponse = UserResponse.builder();
            userResponse.id(user.getId());
            userResponse.name(user.getName());
            userResponse.email(user.getEmail());
            return userResponse.build();
    }

    public List<UserResponse> toResponseCollection(List<User> users) {
        if (users == null) {
            return null;
        }
            List<UserResponse> list = new ArrayList<UserResponse>(users.size());

            for (User user : users) {
                list.add(toResponse(user));
            }

            return list;
    }

}
