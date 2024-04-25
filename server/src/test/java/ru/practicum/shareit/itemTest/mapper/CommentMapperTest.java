package ru.practicum.shareit.itemTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest {

    private CommentMapper commentMapper = new CommentMapper();

    private Comment comment;
    private CommentResponse commentResponse;
    private CommentCreateRequest commentCreateRequest;

    @BeforeEach
    public void create() {

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Комментарий");
        comment.setItem(new Item());
        comment.setAuthor(new User());
        comment.setCreated(LocalDateTime.now());

        commentResponse = new CommentResponse(1L,
                "Комментарий",
                new ItemResponse(1L,
                        "Отвертка",
                        "Крестовая отвертка для саморезов",
                        true,
                        1L,
                        null,
                        null,
                        new ArrayList<>(),
                        1L),
                "name",
                LocalDateTime.now());

        commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.setText("Комментарий");
    }

    @Test
    public void toCommentTest() {

       Comment comment1 = commentMapper.toComment(commentCreateRequest);

        assertEquals("Комментарий", comment1.getText());
    }

    @Test
    public void toResponsetTest() {

        CommentResponse commentResponse1 = commentMapper.toResponse(comment);

        assertEquals("Комментарий", commentResponse1.getText());
    }

    @Test
    public void toResponseCollectionTest() {

        List<CommentResponse> commentResponses = commentMapper.toResponseCollection(List.of(comment));

        assertEquals(1, commentResponses.size());
        assertEquals("Комментарий", commentResponses.get(0).getText());
    }
}
