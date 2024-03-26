package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Comment;
import java.util.List;

public interface CommentRepository {
    List<Comment> findByItemId(Long itemId);

    public Comment createComment(Comment comment);
}
