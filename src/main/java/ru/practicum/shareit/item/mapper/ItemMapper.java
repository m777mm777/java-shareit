package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public List<ItemResponse> toResponseCollection(List<Item> items) {

        List<ItemResponse> itemResponses = new ArrayList<>();

        for (Item item: items) {
            ItemResponse itemResponse = toResponse(item);
            itemResponses.add(itemResponse);
        }

        return itemResponses;
    }

    public Item toItem(ItemCreateRequest request) {
        if (request == null) {
            return null;
        } else {
            Item item = new Item();
            item.setName(request.getName());
            item.setDescription(request.getDescription());
            item.setAvailable(request.getAvailable());
            return item;
        }
    }

    public ItemResponse toResponse(Item item) {
        if (item == null) {
            return null;
        } else {
            ItemResponse.ItemResponseBuilder itemResponse = ItemResponse.builder();
            itemResponse.id(item.getId());
            itemResponse.name(item.getName());
            itemResponse.description(item.getDescription());
            itemResponse.available(item.getAvailable());
            itemResponse.owner(item.getOwner().getId());

            if (item.getQuestion() != null) {
                itemResponse.requestId(item.getQuestion().getId());
            }
            return itemResponse.build();
        }
    }
}
