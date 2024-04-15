package ru.practicum.shareit.questionTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.service.QuestionServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class QuestionServiceImplTestIntegration {

    @Autowired
    private UserService userService;
    @Autowired
    QuestionServiceImpl questionService;

    @Test
    public void getAllQuestionOtherUserTest() {

        User user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userService.create(user);

        User user2 = new User();
        user2.setName("name");
        user2.setEmail("ff@mail.ru");

        userService.create(user2);

        QuestionCreateRequest questionCreateRequest = new QuestionCreateRequest();
        questionCreateRequest.setDescription("Любая отвертка");

        questionService.createQuestion(2L, questionCreateRequest);

        List<QuestionResponse> questions = questionService.getAllQuestionOtherUser(1L, 0, 10);

        assertEquals(1, questions.size());
        assertEquals("Любая отвертка", questions.get(0).getDescription());
    }
}
