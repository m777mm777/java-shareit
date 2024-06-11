package ru.practicum.shareit.UserTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.controller.dto.UserCreateRequest;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserResponse userResponse;
    private UserCreateRequest userCreateRequest;

    @BeforeEach
    public void startTest() {

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userResponse = new UserResponse(1L, "name", "mail1@mail.ru");

        userCreateRequest = new UserCreateRequest("name", "mail1@mail.ru");
    }

    @Test
    public void createTest() throws Exception {

        when(userMapper.toUser(any(UserCreateRequest.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTest() throws Exception {

        Long id = 1L;

        when(userMapper.toUser(any(UserCreateRequest.class))).thenReturn(user);
        when(userService.update(any(User.class), anyLong())).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        mvc.perform(patch("/users/{id}", id)
                        .content(objectMapper.writeValueAsString(userCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdTest() throws Exception {

        Long id = 1L;

        when(userService.update(any(User.class), anyLong())).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        mvc.perform(get("/users/{id}", id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllTest() throws Exception {


        when(userService.getAll()).thenReturn(List.of(user));
        when(userMapper.toResponseCollection(anyList())).thenReturn(List.of(userResponse));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void removeByIdTest() throws Exception {

        Long id = 1L;

        when(userService.removeById(anyLong())).thenReturn(1L);

        mvc.perform(get("/users/{id}", id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }
}
