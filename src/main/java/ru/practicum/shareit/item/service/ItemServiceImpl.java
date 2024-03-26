package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public Item createItem(Long userOwnerId, Item item) {
        userService.findById(userOwnerId);
        item.setOwner(userOwnerId);
        return itemRepository.createItem(item);
    }

    @Override
    public Item update(Long userOwnerId, Long itemId, Item item) {
        userService.findById(userOwnerId);
        item.setId(itemId);
        item.setOwner(userOwnerId);
        Item itemOld = getItemById(itemId);
        if (!item.getOwner().equals(itemOld.getOwner())) {
            throw new ResourceNotFoundException("Item этому пользователю не принадлежит");
        }
        return itemRepository.update(item);
    }

    @Override
    public ItemResponse findById(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));

        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentResponse> commentResponses = commentMapper.toResponseCollection(comments);

        ItemResponse itemResponse = itemMapper.toResponse(item);
        itemResponse.setComments(commentResponses);
        toAssemble(itemId, ownerId, itemResponse);

        return itemResponse;
    }

    @Override
    public List<ItemResponse> getAll(Long userOwnerId) {
        userService.findById(userOwnerId);
        List<Item> items =  itemRepository.getAll(userOwnerId);
        List<ItemResponse> responseItems = itemMapper.toResponseCollection(items);
        for (ItemResponse i: responseItems) {
            List<Comment> comments = commentRepository.findByItemId(i.getId());
            List<CommentResponse> commentResponses = commentMapper.toResponseCollection(comments);
            i.setComments(commentResponses);
            toAssemble(i.getId(), userOwnerId, i);
        }
        return responseItems;
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchItem(text);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));
    }

    private void toAssemble(Long itemId, Long ownerId, ItemResponse itemResponse) {
        List<Booking> bookings = bookingRepository.findByItemIdAndItemOwner(itemId, ownerId);

        bookings.stream()
                .filter(t -> t.getStart().isBefore(LocalDateTime.now()) && !t.getStatus().equals(StatusBooking.REJECTED))
                .max(Comparator.comparing(Booking::getEnd))
                .ifPresent(lastBooking -> itemResponse.setLastBooking(bookingMapper.toInformation(lastBooking, lastBooking.getBooker().getId())));
        bookings.stream()
                .filter(t -> t.getStart().isAfter(LocalDateTime.now()) && !t.getStatus().equals(StatusBooking.REJECTED))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(nextBooking -> itemResponse.setNextBooking(bookingMapper.toInformation(nextBooking, nextBooking.getBooker().getId())));

        List<Comment> comments = commentRepository.findByItemId(itemId);
        itemResponse.setComments(commentMapper.toResponseCollection(comments));
    }

    @Override
    public CommentResponse createComment(Long authorId, Long itemId, CommentCreateRequest request) {
        List<Booking> bookings = bookingRepository.findByItemIdAndBookerId(itemId, authorId).stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        if (bookings.isEmpty()) {
            throw new ValidationException("Вы не можете оставить отзыв");
        }
        Comment comment = commentMapper.toComment(request);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));

        comment.setItem(item);
        User author = userService.findById(authorId);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        commentRepository.createComment(comment);

        CommentResponse commentResponse = commentMapper.toResponse(comment);
        ItemResponse itemResponse = itemMapper.toResponse(item);
        commentResponse.setItemResponse(itemResponse);
        return commentResponse;
    }
}
