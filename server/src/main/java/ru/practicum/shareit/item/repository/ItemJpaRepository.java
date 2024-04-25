package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.user.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {

    Page<Item> findByOwner(User owner, Pageable page);

    @Query("select i from Item i where i.available = true AND (upper(i.name) like upper(concat('%', ?1, '%')) OR " +
            "upper (i.description) like upper(concat('%', ?1, '%')))")
    Page<Item> searchItem(String text, Pageable page);

    List<Item> findByQuestionId(Long questionId);

    List<Item> findAllByQuestionIn(List<Question> questions);
}
