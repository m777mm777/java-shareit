package ru.practicum.shareit.UserTest.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userJpaRepository;

    private User user;

    @BeforeEach
    public void create() {

        user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");
        user = userJpaRepository.save(user);
    }

    @Test
    public void saveTest() {

        User user1 = userJpaRepository.save(user);

        assertEquals("name", user1.getName());
    }
}
