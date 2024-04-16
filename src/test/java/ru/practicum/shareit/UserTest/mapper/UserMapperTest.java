package ru.practicum.shareit.UserTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.controller.dto.UserCreateRequest;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    User user;
    UserResponse userResponse;
    UserCreateRequest userCreateRequest;

    @BeforeEach
    public void create() {

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userResponse = new UserResponse(1L, "name", "mail1@mail.ru");

        userCreateRequest = new UserCreateRequest("name", "mail1@mail.ru");
    }

    @Test
    public void toUserTest() {

        User user1 = userMapper.toUser(userCreateRequest);

        assertEquals("name", user1.getName());
        assertEquals("mail1@mail.ru", user1.getEmail());
    }

    @Test
    public void toResponseTest() {

        UserResponse userResponse1 = userMapper.toResponse(user);

        assertEquals("name", userResponse1.getName());
        assertEquals("mail1@mail.ru", userResponse1.getEmail());
    }

    @Test
    public void toResponseCollectionTest() {

        List<UserResponse> userResponses = userMapper.toResponseCollection(List.of(user));

        assertEquals(1, userResponses.size());
        assertEquals("mail1@mail.ru", userResponses.get(0).getEmail());
    }
}
