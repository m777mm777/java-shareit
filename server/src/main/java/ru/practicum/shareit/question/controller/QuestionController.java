package ru.practicum.shareit.question.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.constants.Constants;
import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.mapper.QuestionMapper;
import ru.practicum.shareit.question.service.QuestionService;

import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @PostMapping
    public QuestionResponse createQuestion(@RequestHeader(Constants.RESPONSEHEADER) Long userCreatorId,
                                          @RequestBody QuestionCreateRequest request) {
        log.info("Create userCreatorId {} request {}", userCreatorId, request);
        return questionMapper.toResponse(questionService.createQuestion(userCreatorId, request));
    }

    @GetMapping
    public List<QuestionResponse> getAllQuestionByCreator(@RequestHeader(Constants.RESPONSEHEADER) Long userCreatorId) {
        log.info("getAllQuestionByCreator userCreatorId {}", userCreatorId);
        return questionService.getAllQuestionByCreator(userCreatorId);
    }

    @GetMapping("/all")
    public List<QuestionResponse> getAllQuestionOtherUser(@RequestHeader(Constants.RESPONSEHEADER) Long userId,
                                                          @RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size) {
        log.info("getAllQuestionOtherUser userId {} from {} size {}", userId, from, size);
        return questionService.getAllQuestionOtherUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public QuestionResponse getQuestionById(@RequestHeader(Constants.RESPONSEHEADER) Long userId,
                                            @PathVariable Long requestId) {
        log.info("getQuestionById userId {} requestId {}", userId, requestId);
        return questionService.getQuestionById(userId, requestId);
    }
}
