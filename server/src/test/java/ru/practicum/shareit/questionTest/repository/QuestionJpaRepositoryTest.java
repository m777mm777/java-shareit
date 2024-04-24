package ru.practicum.shareit.questionTest.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.question.repository.QuestionJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuestionJpaRepositoryTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    private User user;
    private Item item;
    private Question question;
    private static final Sort SORT_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");
    private Pageable page = PageRequest.of(0, 10, Sort.by("created").descending());

    @BeforeEach
    public void createObjects() {

        user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");
        user = userJpaRepository.save(user);

        question = new Question();
        question.setDescription("Нужна любая отвертка");
        question.setCreator(user);
        question.setCreated(LocalDateTime.now());
        question = questionJpaRepository.save(question);

        item = new Item();
        item.setName("Отвертка");
        item.setDescription("Крестовая отвертка для саморезов");
        item.setAvailable(true);
        item.setOwner(user);
        item.setQuestion(question);
        item = itemJpaRepository.save(item);

    }

    @Test
    public void findByCreatorIdTest() {

        List<Question> questions = questionJpaRepository.findByCreatorId(user.getId(), SORT_CREATED_DESC);

        assertEquals(1, questions.size());
        assertEquals("Нужна любая отвертка", questions.get(0).getDescription());
    }

    @Test
    public void findByCreatorIdNotTest() {

        User user1 = new User();
        user1.setName("name");
        user1.setEmail("44@mail.ru");
        user1 = userJpaRepository.save(user1);

        List<Question> questions = questionJpaRepository.findByCreatorIdNot(user1.getId(), page).getContent();

        assertEquals(1, questions.size());
        assertEquals("Нужна любая отвертка", questions.get(0).getDescription());
    }
}
