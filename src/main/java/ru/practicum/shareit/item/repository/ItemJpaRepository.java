package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerOrderByIdAsc(Long idOwner);

    @Query("select i from Item i where i.available = true AND (upper(i.name) like upper(concat('%', ?1, '%')) OR " +
            "upper (i.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItem(String text);
}
