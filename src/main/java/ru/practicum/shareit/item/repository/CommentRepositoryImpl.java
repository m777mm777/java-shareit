package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public List<Comment> findByItemId(Long itemId) {
        return commentJpaRepository.findByItemId(itemId);
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentJpaRepository.save(comment);
    }
}
