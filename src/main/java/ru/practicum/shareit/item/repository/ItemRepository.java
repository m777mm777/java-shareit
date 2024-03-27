package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    public Item createItem(Item item);

    public Item update(Item item);

    public Optional<Item> findById(Long itemId);

    public List<Item> getAll(Long userOwnerId);

    public List<Item> searchItem(String text);
}
