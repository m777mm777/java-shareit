package ru.practicum.shareit.itemTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemServiceImplTestIntegration {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Test
    public void getAllItemsByOwnerTest() {

        User user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                null);

        ItemCreateRequest itemCreateRequest2 = new ItemCreateRequest("Шуруповерт",
                "Шуруповерт аккумуляторный профессиональный",
                true,
                null);

        User user1 = userService.create(user);
        Item item = itemService.createItem(1L, itemCreateRequest);
        itemService.createItem(user1.getId(), itemCreateRequest2);

        List<ItemResponse> itemResponses = itemService.getAllItemsByOwner(user1.getId(), 0, 10);

        assertEquals(2, itemResponses.size());
        assertEquals("Отвертка", itemResponses.get(0).getName());
        assertEquals("Шуруповерт", itemResponses.get(1).getName());
    }
}
