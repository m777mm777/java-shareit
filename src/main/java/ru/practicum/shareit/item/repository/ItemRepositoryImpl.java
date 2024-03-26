package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemJpaRepository itemJpaRepository;

    @Override
    public Item createItem(Item item) {
        return itemJpaRepository.save(item);
    }

    @Override
    public Item update(Item item) {
        Item oldItem = findById(item.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Вещь не найдена"));

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

        return itemJpaRepository.save(item);
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return itemJpaRepository.findById(itemId);
    }

    @Override
    public List<Item> getAll(Long userOwnerId) {
        return itemJpaRepository.findByOwnerOrderByIdAsc(userOwnerId);
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> allItems = itemJpaRepository.findAll();
        final String textUpperCase = ".*" + text.toUpperCase() + ".*";

        List<Item> items = allItems.stream()
                .filter(e -> (e.getName()
                        .toUpperCase()
                        .matches(textUpperCase) || e.getDescription()
                        .toUpperCase().matches(textUpperCase)) && e.getAvailable().equals(true))
                .collect(Collectors.toList());

        return items;
    }
}
