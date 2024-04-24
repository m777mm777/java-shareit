package ru.practicum.shareit.itemTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {

    ItemMapper itemMapper = new ItemMapper();

    User user;
    Item item;
    ItemResponse itemResponse;
    ItemCreateRequest request;

    @BeforeEach
    public void create() {

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("Отвертка");
        item.setDescription("Крестовая отвертка для саморезов");
        item.setAvailable(true);
        item.setOwner(user);

        itemResponse = new ItemResponse(1L,
                "Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L,
                null,
                null,
                new ArrayList<>(),
                1L);

        request = new ItemCreateRequest("Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L);
    }

    @Test
    public void toResponseCollectionTest() {

       List<ItemResponse> itemResponses = itemMapper.toResponseCollection(List.of(item));

        assertEquals(1, itemResponses.size());
        assertEquals("Отвертка", itemResponses.get(0).getName());
    }

    @Test
    public void toItemTest() {

        Item item1 = itemMapper.toItem(request);

        assertEquals("Отвертка", item1.getName());
        assertEquals("Крестовая отвертка для саморезов", item1.getDescription());
    }

    @Test
    public void toResponseTest() {

        ItemResponse itemResponse1 = itemMapper.toResponse(item);

        assertEquals("Отвертка", itemResponse1.getName());
        assertEquals("Крестовая отвертка для саморезов", itemResponse1.getDescription());
    }
}
