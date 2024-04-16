package ru.practicum.shareit.itemTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.controller.constants.Constants;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private MockMvc mvc;

    User user;
    Item item;
    ItemResponse itemResponse;
    ItemCreateRequest request;
    CommentCreateRequest commentCreateRequest;

    @BeforeEach
    public void startTest() {

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("Отвертка");
        item.setDescription("Крестовая отвертка для саморезов");
        item.setAvailable(true);
        item.setOwner(user);

        itemResponse = new ItemResponse(1L,
                "Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L,
                null,
                null,
                new ArrayList<>(),
                1L);

        request = new ItemCreateRequest("Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L);

        commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.setText("Комментарий");
    }

    @Test
    public void createTest() throws Exception {

        when(itemService.createItem(anyLong(), any(ItemCreateRequest.class)))
                .thenReturn(item);

        when(itemMapper.toResponse(item)).thenReturn(itemResponse);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.available", is(request.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(request.getRequestId()), Long.class));
    }

    @Test
    public void createTestBadRequest() throws Exception {

        when(itemService.createItem(anyLong(), any(ItemCreateRequest.class)))
                .thenReturn(item);

        when(itemMapper.toResponse(item)).thenReturn(itemResponse);

        ItemCreateRequest itemCreateRequestBad = new ItemCreateRequest(null,
                null,
                null,
                null);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemCreateRequestBad))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTest() throws Exception {

        Long itemId = 1L;

        when(itemMapper.toItem(any(ItemCreateRequest.class))).thenReturn(item);

        when(itemService.update(anyLong(), anyLong(), any(Item.class)))
                .thenReturn(item);

        when(itemMapper.toResponse(item)).thenReturn(itemResponse);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.available", is(request.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(request.getRequestId()), Long.class));
    }

    @Test
    public void findByIdTest() throws Exception {

        Long itemId = 1L;

        when(itemService.findById(anyLong(), anyLong()))
                .thenReturn(itemResponse);

        mvc.perform(get("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllByOwnerTest() throws Exception {

        Integer from = 0;
        Integer size = 10;

        when(itemService.getAllItemsByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemResponse));

        mvc.perform(get("/items")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllByOwnerTestNotValid() throws Exception {

        Integer from = -1;
        Integer size = 10;

        when(itemService.getAllItemsByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemResponse));

        mvc.perform(get("/items")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void searchItemTest() throws Exception {

        Integer from = 0;
        Integer size = 10;
        String text = "текст";

        when(itemService.searchItem(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(item));

        mvc.perform(get("/items/search")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .param("text", text)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void searchItemTestNotValid() throws Exception {

        Integer from = 0;
        Integer size = -10;
        String text = "текст";

        when(itemService.searchItem(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(item));

        mvc.perform(get("/items/search")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .param("text", text)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void createCommentTest() throws Exception {

        Long itemId = 1L;

        when(itemService.findById(anyLong(), anyLong()))
                .thenReturn(itemResponse);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void createCommentTestBadRequest() throws Exception {

        Long itemId = 1L;

        when(itemService.findById(anyLong(), anyLong()))
                .thenReturn(itemResponse);

        CommentCreateRequest createRequest = new CommentCreateRequest();

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(createRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }
}
