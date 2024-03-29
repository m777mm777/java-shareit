package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item addNewItem(Long userOwnerId, Item item) {
        userService.findById(userOwnerId);
        item.setOwner(userOwnerId);
        return itemStorage.addNewItem(item);
    }

    @Override
    public Item update(Long userOwnerId, Long itemId, Item item) {
        userService.findById(userOwnerId);
        item.setId(itemId);
        item.setOwner(userOwnerId);
        Item itemOld = findById(itemId);
        if (!item.getOwner().equals(itemOld.getOwner())) {
            throw new ResourceNotFoundException("Item этому пользователю не принадлежит");
        }
        return itemStorage.update(item);
    }

    @Override
    public Item findById(Long itemId) {
        return itemStorage.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));
    }

    @Override
    public List<Item> getAll(Long userOwnerId) {
        userService.findById(userOwnerId);
        return itemStorage.getAll(userOwnerId);
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemStorage.searchItem(text);
    }

}
