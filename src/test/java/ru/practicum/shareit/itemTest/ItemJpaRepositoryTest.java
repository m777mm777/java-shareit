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
    public void findByOwnerPositive() {

        List<Item> items = itemJpaRepository.findByOwner(user, page).getContent();

        assertEquals(1, items.size());
        assertEquals("Отвертка", items.get(0).getName());
    }

    @Test
    public void findByOwnerNegative() {

        user = new User();
        user.setId(99L);
        user.setName("noSave");
        user.setEmail("noSave@mail.ru");
        List<Item> items = itemJpaRepository.findByOwner(user, page).getContent();

        assertEquals(0, items.size());
    }

    @Test
    public void searchItemTest() {


        List<Item> items = itemJpaRepository.searchItem("Отве", page).getContent();

        assertEquals(1, items.size());
        assertEquals("Отвертка", items.get(0).getName());
    }

    @Test
    public void findByQuestionIdTest() {


        List<Item> items = itemJpaRepository.findByQuestionId(question.getId());

        assertEquals(1, items.size());
    }

    @Test
    public void findAllByQuestionInTest() {

        List<Item> items = itemJpaRepository.findAllByQuestionIn(List.of(question));

        assertEquals(1, items.size());
    }
}
