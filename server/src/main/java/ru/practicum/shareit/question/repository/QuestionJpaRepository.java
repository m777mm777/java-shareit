package ru.practicum.shareit.question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.question.model.Question;

import java.util.List;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {

    List<Question> findByCreatorId(Long userCreatorId, Sort sort);

    Page<Question> findByCreatorIdNot(Long userId, Pageable page);
}
