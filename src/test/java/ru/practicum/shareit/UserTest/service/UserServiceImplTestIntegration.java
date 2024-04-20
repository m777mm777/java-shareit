package ru.practicum.shareit.UserTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceImplTestIntegration {

    @Autowired
    private UserJpaRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void updateTest() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userRepository.save(user);

        User userNew = new User();
        userNew.setName("nameNew");

        User user1 = userService.update(userNew, 1L);

        assertEquals(1, user1.getId());
        assertEquals("nameNew", user1.getName());
        assertEquals("mail1@mail.ru", user1.getEmail());
    }
}
