package ru.practicum.shareit.question.service;

import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.model.Question;

import java.util.List;

public interface QuestionService {

    public Question createQuestion(Long userCreatorId, QuestionCreateRequest request);

    public List<QuestionResponse> getAllQuestionByCreator(Long userCreatorId);

    public List<QuestionResponse> getAllQuestionOtherUser(Long userId, Integer from, Integer size);

    public QuestionResponse getQuestionById(Long userId, Long questionId);
}
