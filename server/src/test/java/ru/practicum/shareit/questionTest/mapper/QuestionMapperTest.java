package ru.practicum.shareit.questionTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.mapper.QuestionMapper;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionMapperTest {

    private UserMapper userMapper = new UserMapper();
    private ItemMapper itemMapper = new ItemMapper();
    private QuestionMapper questionMapper = new QuestionMapper(userMapper);

    private Question question;
    private QuestionCreateRequest questionCreateRequest;
    private QuestionResponse questionResponse;

    @BeforeEach
    public void create() {

        question = new Question();
        question.setId(1L);
        question.setDescription("Любая отвертка");
        question.setCreator(new User());
        question.setCreated(LocalDateTime.now());

        questionCreateRequest = new QuestionCreateRequest();
        questionCreateRequest.setDescription("Любая отвертка");

        questionResponse = new QuestionResponse(1L,
                "Любая отвертка",
                new UserResponse(1L, "Name", "test@mail.ru"),
                LocalDateTime.now(),
                new ArrayList<>());
    }

    @Test
    public void toResponseCollectionTest() {

        List<QuestionResponse> questionResponses = questionMapper.toResponseCollection(List.of(question));

        assertEquals(1, questionResponses.size());
        assertEquals("Любая отвертка", questionResponses.get(0).getDescription());
    }

    @Test
    public void toQuestionTest() {

        Question question1 = questionMapper.toQuestion(questionCreateRequest);

        assertEquals("Любая отвертка", question1.getDescription());
    }

    @Test
    public void toResponseTest() {

        QuestionResponse questionResponse1 = questionMapper.toResponse(question);

        assertEquals("Любая отвертка", questionResponse1.getDescription());
    }
}
