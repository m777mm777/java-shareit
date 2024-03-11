package ru.practicum.shareit.item.storage.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemDbStorage implements ItemStorage {

    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();
    private Map<Long, Item> storage = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item addNewItem(Item item) {
        Item modified = generatedId(item);
        storage.put(modified.getId(), modified);
        putUserItemIndex(modified);
        return modified;
    }

    @Override
    public Item update(Item item) {
        Item updateItem = toInclude(item);
        storage.put(updateItem.getId(), updateItem);
        putUserItemIndex(updateItem);
        return updateItem;
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return Optional.of(storage.get(itemId));
    }

    @Override
    public List<Item> getAll(Long userOwnerId) {
      return   userItemIndex.get(userOwnerId);
    }

    @Override
    public List<Item> searchItem(String text) {
        final String textUpperCase = ".*" + text.toUpperCase() + ".*";

        List<Item> items = storage.values().stream()
                .filter(e -> (e.getName()
                        .toUpperCase()
                        .matches(textUpperCase) || e.getDescription()
                        .toUpperCase().matches(textUpperCase)) && e.getAvailable().equals(true))
                .collect(Collectors.toList());

        return items;
    }

    private Item generatedId(Item item) {
        item.setId(++id);
        return item;
    }

    private Item toInclude(Item item) {
        Item oldItem = storage.get(item.getId());

        if ((item.getName() == null) || item.getName().isBlank()) {
            item.setName(oldItem.getName());
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription(oldItem.getDescription());
        }

        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }

        item.setOwner(oldItem.getOwner());

        return item;
    }

    private void putUserItemIndex(Item item) {
        final List<Item> items = userItemIndex.computeIfAbsent(item.getOwner(), k -> new ArrayList<>());
        if (items.size() == 0) {
            items.add(item);
            userItemIndex.put(item.getId(), items);
        } else {
            List<Item> uniqItems = items.stream()
                    .filter(e -> !item.getId().equals(e.getId()))
                    .collect(Collectors.toList());
            uniqItems.add(item);
            userItemIndex.put(item.getId(), uniqItems);
        }
    }
}
