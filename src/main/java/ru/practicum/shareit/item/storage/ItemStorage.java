package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    public Item addNewItem(Item item);

    public Item update(Item item);

    public Item findById(Long itemId);

    public List<Item> getAll(Long userOwnerId);

    public List<Item> searchItem(String text);
}
