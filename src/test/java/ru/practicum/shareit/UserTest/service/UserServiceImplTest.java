package ru.practicum.shareit.UserTest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.controller.dto.UserCreateRequest;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserJpaRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;
    private UserCreateRequest userCreateRequest;

    @BeforeEach
    public void createObjects() {

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userResponse = new UserResponse(1L,
                "name",
                "mail1@mail.ru");

        userCreateRequest = new UserCreateRequest("name", "mail1@mail.ru");
    }

    @Test
    public void createTest() {

        when(userRepository.save(any(User.class))).thenReturn(user);

        User user1 = userService.create(user);

        assertEquals(1, user1.getId());
    }

    @Test
    public void updateTest() {

        User userNew = new User();
        userNew.setName("nameNew");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(userNew);

        User user1 = userService.update(userNew, 1L);

        assertEquals(1, user1.getId());
        assertEquals("nameNew", user1.getName());
    }

    @Test
    public void findByIdTest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User user1 = userService.findById(1L);

        assertEquals(1, user1.getId());
    }

    @Test
    public void getAllTest() {

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAll();

        assertEquals(1, users.size());
    }

    @Test
    public void removeByIdest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Long usersId = userService.removeById(1L);

        assertEquals(1, usersId);
    }
}
