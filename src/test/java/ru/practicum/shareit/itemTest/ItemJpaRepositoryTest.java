package ru.practicum.shareit.itemTest;

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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemJpaRepositoryTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    private User user;
    private Item item;
    private Question question;
    private Pageable page = PageRequest.of(0, 10, Sort.by("id"));

    @BeforeEach
    public void createObjects() {

        user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");
        user = userJpaRepository.save(user);

        question = new Question();
        question.setDescription("Любая отвертка нужна");
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
    @Order(1)
    public void findByOwnerPositive() {

        List<Item> items = itemJpaRepository.findByOwner(user, page).getContent();

        assertEquals(1, items.size());
        assertEquals("Отвертка", items.get(0).getName());
        assertEquals(1, items.get(0).getOwner().getId());
    }

    @Test
    @Order(2)
    public void findByOwnerNegative() {

        user = new User();
        user.setId(99L);
        user.setName("noSave");
        user.setEmail("noSave@mail.ru");
        List<Item> items = itemJpaRepository.findByOwner(user, page).getContent();

        assertEquals(0, items.size());
    }

    @Test
    @Order(3)
    public void searchItemTest() {


        List<Item> items = itemJpaRepository.searchItem("Отве", page).getContent();

        assertEquals(1, items.size());
        assertEquals("Отвертка", items.get(0).getName());
    }

    @Test
    @Order(4)
    public void findByQuestionIdTest() {


        List<Item> items = itemJpaRepository.findByQuestionId(4L);

        assertEquals(1, items.size());
        assertEquals("Отвертка", items.get(0).getName());
        assertEquals(4, items.get(0).getQuestion().getId());
    }

    @Test
    @Order(5)
    public void findAllByQuestionInTest() {

        List<Item> items = itemJpaRepository.findAllByQuestionIn(List.of(question));

        assertEquals(1, items.size());
        assertEquals(5, items.get(0).getQuestion().getId());
    }
}
