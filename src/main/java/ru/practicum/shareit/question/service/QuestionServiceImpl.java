package ru.practicum.shareit.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.mapper.QuestionMapper;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.question.repository.QuestionJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionJpaRepository repository;
    private final QuestionMapper questionMapper;
    private final UserService userService;
    private final ItemJpaRepository itemJpaRepository;
    private final ItemMapper itemMapper;
    private static final Sort SORT_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");

    @Override
    @Transactional
    public Question createQuestion(Long userCreatorId, QuestionCreateRequest request) {
        User creator = userService.findById(userCreatorId);
        Question question = questionMapper.toQuestion(request);
        question.setCreated(LocalDateTime.now().withNano(0));
        question.setCreator(creator);
        repository.save(question);
        return question;
    }

    @Override
    public List<Question> getAllQuestionByCreator(Long userCreatorId) {
        userService.findById(userCreatorId);
        List<Question> questions = repository.findByCreatorId(userCreatorId, SORT_CREATED_DESC);

        Map<Long, List<Item>> items = itemJpaRepository.findAllByQuestionIn(questions)
                .stream()
                .collect(groupingBy((Item i) -> i.getQuestion().getId(), toList()));

        for (Question question: questions) {
            question.setItems(items.get(question.getId()));
        }

        return questions;
    }

    @Override
    public List<Question> getAllQuestionOtherUser(Long userId, Integer from, Integer size) {
        userService.findById(userId);

        if (from < 0 || size < 0) {
            throw new ValidationException("Отрицательные значения страниц");
        }

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("created").descending());
        List<Question> questions = repository.findByCreatorIdNot(userId, page).getContent();

        Map<Long, List<Item>> items = itemJpaRepository.findAllByQuestionIn(questions)
                .stream()
                .collect(groupingBy((Item i) -> i.getQuestion().getId(), toList()));

        for (Question question: questions) {
            question.setItems(items.get(question.getId()));
        }

        return questions;
    }

    @Override
    public QuestionResponse getQuestionById(Long userId, Long questionId) {
        userService.findById(userId);
        Question question = repository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question по такому id нет"));

        QuestionResponse questionResponse = questionMapper.toResponse(question);

        List<Item> items = itemJpaRepository.findByQuestionId(questionId);

        if (items == null) {
            questionResponse.setItems(new ArrayList<>());
        } else {
            questionResponse.setItems(itemMapper.toResponseCollection(items));
        }

        return questionResponse;
    }
}
