package ru.practicum.shareit.UserTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserCreateRequest;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserClient userClient;

    UserCreateRequest userCreateRequest;
    ResponseEntity<Object> response;

    @BeforeEach
    public void startTest() {

        userCreateRequest = new UserCreateRequest("name", "mail1@mail.ru");
        response = ResponseEntity.noContent().build();
    }

    @Test
    public void createTest() throws Exception {

        when(userClient.create(any(UserCreateRequest.class))).thenReturn(response);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createTestBadRequest() throws Exception {

        when(userClient.create(any(UserCreateRequest.class))).thenReturn(response);

        UserCreateRequest userCreateRequest1 = new UserCreateRequest("", "ff");
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCreateRequest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTest() throws Exception {

        Long id = 1L;

        when(userClient.update(any(UserCreateRequest.class), anyLong())).thenReturn(response);

        mvc.perform(patch("/users/{id}", id)
                        .content(objectMapper.writeValueAsString(userCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateTestBadRequest() throws Exception {

        Long id = 1L;

        when(userClient.update(any(UserCreateRequest.class), anyLong())).thenReturn(response);

        UserCreateRequest userCreateRequest1 = new UserCreateRequest("", "ff");

        mvc.perform(patch("/users/{id}", id)
                        .content(objectMapper.writeValueAsString(userCreateRequest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findByIdTest() throws Exception {

        Long id = 1L;

        when(userClient.update(any(UserCreateRequest.class), anyLong())).thenReturn(response);

        mvc.perform(get("/users/{id}", id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllTest() throws Exception {


        when(userClient.getAll()).thenReturn(response);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeByIdTest() throws Exception {

        Long id = 1L;

        when(userClient.removeById(anyLong())).thenReturn(response);

        mvc.perform(get("/users/{id}", id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }
}
