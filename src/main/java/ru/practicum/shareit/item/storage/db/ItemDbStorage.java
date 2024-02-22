package ru.practicum.shareit.item.storage.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemDbStorage implements ItemStorage {

    private Map<Long, Item> storage = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item addNewItem(Item item) {
        Item modified = generatedId(item);
        storage.put(modified.getId(), modified);
        return modified;
    }

    @Override
    public Item update(Item item) {
        Item updateItem = toInclude(item);
        storage.put(updateItem.getId(), updateItem);
        return updateItem;
    }

    @Override
    public Item findById(Long itemId) {
        if (!storage.containsKey(itemId)) {
            throw new ResourceNotFoundException("Item по такому id нет");
        }
        return storage.get(itemId);
    }

    @Override
    public List<Item> getAll(Long userOwnerId) {
        List<Item> items = storage.values().stream()
                .filter(e -> userOwnerId.equals(e.getOwner()))
                .collect(Collectors.toList());
        return items;
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> items = storage.values().stream()
                .filter(e -> (e.getName()
                        .toUpperCase()
                        .matches(".*" + text.toUpperCase() + ".*") | e.getDescription()
                        .toUpperCase().matches(".*" + text.toUpperCase() + ".*")) & e.getAvailable().equals(true))
                .collect(Collectors.toList());

        return items;
    }

    private Item generatedId(Item item) {
        item.setId(++id);
        return item;
    }

    private Item toInclude(Item item) {
        Item oldItem = storage.get(item.getId());

        if (item.getName() == null) {
            item.setName(oldItem.getName());
        }

        if (item.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }

        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }

        item.setOwner(oldItem.getOwner());

        return item;
    }
}
