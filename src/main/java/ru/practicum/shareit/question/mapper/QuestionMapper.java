package ru.practicum.shareit.question.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionMapper {

    private final UserMapper userMapper;

    public List<QuestionResponse> toResponseCollection(List<Question> questions) {

        List<QuestionResponse> questionResponses = new ArrayList<>();

        for (Question question: questions) {
            QuestionResponse questionResponse = toResponse(question);
            questionResponses.add(questionResponse);
        }

        return questionResponses;
    }

    public Question toQuestion(QuestionCreateRequest request) {
        if (request == null) {
            return null;
        } else {
            Question question = new Question();
            question.setDescription(request.getDescription());
            return question;
        }
    }

    public QuestionResponse toResponse(Question question) {
        if (question == null) {
            return null;
        } else {
            QuestionResponse.QuestionResponseBuilder questionResponse = QuestionResponse.builder();
            questionResponse.id(question.getId());
            questionResponse.description(question.getDescription());
            questionResponse.creator(userMapper.toResponse(question.getCreator()));
            questionResponse.created(question.getCreated());
            questionResponse.items(new ArrayList<>());

            return questionResponse.build();
        }
    }

}
