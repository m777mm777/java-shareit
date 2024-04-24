package ru.practicum.shareit.question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.createAndUpdate.Create;
import ru.practicum.shareit.question.dto.QuestionCreateRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionClient questionClient;

    @PostMapping
    public ResponseEntity<Object> createQuestion(@RequestHeader(Constants.RESPONSEHEADER) Long userCreatorId,
                                                 @Validated(Create.class) @RequestBody QuestionCreateRequest request) {
        log.info("Create userCreatorId {} request {}", userCreatorId, request);
        return questionClient.createQuestion(userCreatorId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllQuestionByCreator(@RequestHeader(Constants.RESPONSEHEADER) Long userCreatorId) {
        log.info("getAllQuestionByCreator userCreatorId {}", userCreatorId);
        return questionClient.getAllQuestionByCreator(userCreatorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllQuestionOtherUser(@RequestHeader(Constants.RESPONSEHEADER) Long userId,
                                                          @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("getAllQuestionOtherUser userId {} from {} size {}", userId, from, size);
        return questionClient.getAllQuestionOtherUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getQuestionById(@RequestHeader(Constants.RESPONSEHEADER) Long userId,
                                            @PathVariable Long requestId) {
        log.info("getQuestionById userId {} requestId {}", userId, requestId);
        return questionClient.getQuestionById(userId, requestId);
    }
}
