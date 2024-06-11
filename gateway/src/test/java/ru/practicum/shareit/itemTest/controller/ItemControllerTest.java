package ru.practicum.shareit.itemTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mvc;

    private ItemCreateRequest request;
    private CommentCreateRequest commentCreateRequest;
    private ResponseEntity<Object> response;

    @BeforeEach
    public void startTest() {

        request = new ItemCreateRequest("Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L);

        commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.setText("Комментарий");

        response = ResponseEntity.noContent().build();
    }

    @Test
    public void createTest() throws Exception {

        when(itemClient.create(anyLong(), any(ItemCreateRequest.class)))
                .thenReturn(response);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createTestBadRequest() throws Exception {

        when(itemClient.create(anyLong(), any(ItemCreateRequest.class)))
                .thenReturn(response);

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

        when(itemClient.update(anyLong(), any(ItemCreateRequest.class), anyLong()))
                .thenReturn(response);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void findByIdTest() throws Exception {

        Long itemId = 1L;

        when(itemClient.findById(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllByOwnerTest() throws Exception {

        Integer from = 0;
        Integer size = 10;

        when(itemClient.getAllByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/items")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllByOwnerTestNotValid() throws Exception {

        Integer from = -1;
        Integer size = 10;

        when(itemClient.getAllByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/items")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void searchItemTest() throws Exception {

        Integer from = 0;
        Integer size = 10;
        String text = "текст";

        when(itemClient.searchItem(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/items/search")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .param("text", text)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void searchItemTestNotValid() throws Exception {

        Integer from = 0;
        Integer size = -10;
        String text = "текст";

        when(itemClient.searchItem(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/items/search")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .param("text", text)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCommentTest() throws Exception {

        Long itemId = 1L;

        when(itemClient.findById(anyLong(), anyLong()))
                .thenReturn(response);

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

        when(itemClient.findById(anyLong(), anyLong()))
                .thenReturn(response);

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
