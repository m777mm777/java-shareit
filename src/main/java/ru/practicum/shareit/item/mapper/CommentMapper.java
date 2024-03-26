package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentMapper {

    public Comment toComment(CommentCreateRequest request) {
        if (request == null) {
            return null;
        }

        Comment comment = new Comment();

        comment.setId(request.getId());
        comment.setText(request.getText());
        comment.setCreated(request.getCreated());

        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponse.CommentResponseBuilder commentResponse = CommentResponse.builder();

        commentResponse.id(comment.getId());
        commentResponse.text(comment.getText());
        commentResponse.authorName(comment.getAuthor().getName());
        commentResponse.created(comment.getCreated());

        return commentResponse.build();
    }

    public List<CommentResponse> toResponseCollection(List<Comment> comments) {
        if (comments == null) {
            return null;
        }

        List<CommentResponse> list = new ArrayList<CommentResponse>(comments.size());
        for (Comment comment : comments) {
            list.add(toResponse(comment));
        }

        return list;
    }
}
