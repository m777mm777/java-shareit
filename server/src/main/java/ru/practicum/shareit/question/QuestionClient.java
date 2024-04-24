package ru.practicum.shareit.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.question.dto.QuestionCreateRequest;

import java.util.Map;

@Component
public class QuestionClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public QuestionClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createQuestion(Long userCreatorId, QuestionCreateRequest request) {
        return post("", userCreatorId, request);
    }

    public ResponseEntity<Object> getAllQuestionByCreator(Long userCreatorId) {
        return get("", userCreatorId);
    }

    public ResponseEntity<Object> getAllQuestionOtherUser(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getQuestionById(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }

}
