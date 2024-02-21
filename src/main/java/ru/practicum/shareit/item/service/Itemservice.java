package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface Itemservice {

    public Item addNewItem(Long userOwnerId, Item item);

    public Item update(Long userOwnerId, Long itemId, Item item);

    public Item findById(Long itemId);

    public List<Item> getAll(Long userOwnerId);

    public List<Item> searchItem(String text);
}
