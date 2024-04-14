package ru.practicum.shareit.questionTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.constants.Constants;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.question.controller.QuestionController;
import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.mapper.QuestionMapper;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.question.service.QuestionService;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private QuestionService questionService;
    @MockBean
    private QuestionMapper questionMapper;

    private User user;
    private UserResponse userResponse;
    private ItemResponse itemResponse;
    private Question question;
    private QuestionResponse questionResponse;
    private QuestionCreateRequest questionCreateRequest;

    @BeforeEach
    public void startTest() {

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userResponse = new UserResponse(1L,
                "name",
                "mail1@mail.ru");

        question = new Question();
        question.setId(1L);
        question.setDescription("Любая отвертка нужна");
        question.setCreator(user);
        question.setCreated(LocalDateTime.now());

        itemResponse = new ItemResponse(1L,
                "Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L,
                null,
                null,
                null,
                1L);

        questionResponse = new QuestionResponse(1L,
                "Любая отвертка нужна",
                userResponse,
                LocalDateTime.now(),
                List.of(itemResponse));

        questionCreateRequest = new QuestionCreateRequest();
        questionCreateRequest.setDescription("Любая отвертка нужна");
    }

    @Test
    public void createQuestionTest() throws Exception {

        when(questionService.createQuestion(anyLong(), any(QuestionCreateRequest.class)))
                .thenReturn(question);

        when(questionMapper.toResponse(question)).thenReturn(questionResponse);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(questionCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(questionCreateRequest.getDescription())));
    }

    @Test
    public void getAllQuestionByCreatorTest() throws Exception {

        when(questionService.getAllQuestionByCreator(anyLong()))
                .thenReturn(List.of(question));

        when(questionMapper.toResponseCollection(anyList())).thenReturn(List.of(questionResponse));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllQuestionOtherUserTest() throws Exception {

        Integer from = 0;
        Integer size = 10;

        when(questionService.getAllQuestionOtherUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(question));

        when(questionMapper.toResponse(question)).thenReturn(questionResponse);

        mvc.perform(get("/requests/all")
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getQuestionByIdTest() throws Exception {

        Long requestId = 1L;

        when(questionService.getQuestionById(anyLong(),anyLong()))
                .thenReturn(questionResponse);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }
}

