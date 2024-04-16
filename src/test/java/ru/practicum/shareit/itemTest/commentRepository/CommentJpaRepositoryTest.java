package ru.practicum.shareit.itemTest.commentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CommentJpaRepositoryTest {

    @Autowired
    CommentJpaRepository commentJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    ItemJpaRepository itemJpaRepository;

    private User user;
    private Item item;
    private Comment comment;

    @BeforeEach
    public void create() {

        user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");
        user = userJpaRepository.save(user);

        item = new Item();
        item.setName("Отвертка");
        item.setDescription("Крестовая отвертка для саморезов");
        item.setAvailable(true);
        item.setOwner(user);
        item = itemJpaRepository.save(item);

        comment = new Comment();
        comment.setText("Коммент");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment = commentJpaRepository.save(comment);
    }

    @Test
    public void findByItemIdTest() {

       List<Comment> comments = commentJpaRepository.findByItemId(item.getId());

        assertEquals(1, comments.size());
        assertEquals("Коммент", comments.get(0).getText());
    }
}
