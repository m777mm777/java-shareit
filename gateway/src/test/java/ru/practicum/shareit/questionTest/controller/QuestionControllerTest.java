package ru.practicum.shareit.questionTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.question.QuestionClient;
import ru.practicum.shareit.question.QuestionController;
import ru.practicum.shareit.question.dto.QuestionCreateRequest;
import ru.practicum.shareit.constants.Constants;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private QuestionClient questionClient;

    private QuestionCreateRequest request;
    private ResponseEntity<Object> response;

    @BeforeEach
    public void startTest() {
        response = ResponseEntity.noContent().build();

        request = new QuestionCreateRequest();
        request.setDescription("Любая отвертка нужна");
    }

    @Test
    public void createQuestionTest() throws Exception {

        when(questionClient.createQuestion(anyLong(), any(QuestionCreateRequest.class)))
                .thenReturn(response);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createQuestionTestBadRequest() throws Exception {

        when(questionClient.createQuestion(anyLong(), any(QuestionCreateRequest.class)))
                .thenReturn(response);

        QuestionCreateRequest questionCreateRequest1 = new QuestionCreateRequest();

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(questionCreateRequest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllQuestionByCreatorTest() throws Exception {

        when(questionClient.getAllQuestionByCreator(anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllQuestionOtherUserTest() throws Exception {

        Integer from = 0;
        Integer size = 10;

        when(questionClient.getAllQuestionOtherUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/requests/all")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllQuestionOtherUserTestNotValid() throws Exception {

        Integer from = -1;
        Integer size = -10;

        when(questionClient.getAllQuestionOtherUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/requests/all")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getQuestionByIdTest() throws Exception {

        Long requestId = 1L;

        when(questionClient.getQuestionById(anyLong(),anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }
}

