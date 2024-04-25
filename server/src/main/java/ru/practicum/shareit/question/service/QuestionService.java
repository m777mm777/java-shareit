package ru.practicum.shareit.question.service;

import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.model.Question;

import java.util.List;

public interface QuestionService {

    Question createQuestion(Long userCreatorId, QuestionCreateRequest request);

    List<QuestionResponse> getAllQuestionByCreator(Long userCreatorId);

    List<QuestionResponse> getAllQuestionOtherUser(Long userId, Integer from, Integer size);

    QuestionResponse getQuestionById(Long userId, Long questionId);
}
