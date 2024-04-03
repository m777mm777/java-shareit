package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemJpaRepository itemRepository;
    private final UserService userService;
    private final BookingJpaRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentJpaRepository commentRepository;
    private final CommentMapper commentMapper;
    private static final Sort SORT_START_DESC = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public Item createItem(Long userOwnerId, Item item) {
        userService.findById(userOwnerId);
        item.setOwner(userOwnerId);
        return itemRepository.save(item);
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

        if ((item.getName() == null) || item.getName().isBlank()) {
            item.setName(itemOld.getName());
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription(itemOld.getDescription());
        }

        if (item.getAvailable() == null) {
            item.setAvailable(itemOld.getAvailable());
        }

        item.setOwner(itemOld.getOwner());

        return itemRepository.save(item);
    }

    @Override
    public ItemResponse findById(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));

        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentResponse> commentResponses = commentMapper.toResponseCollection(comments);

        ItemResponse itemResponse = itemMapper.toResponse(item);
        itemResponse.setComments(commentResponses);

        List<Booking> bookings = bookingRepository.findByItemIdAndItemOwner(itemId, ownerId);

        toAssemble(bookings, itemResponse);

        return itemResponse;
    }

    @Override
    public List<ItemResponse> getAllItemsByOwner(Long userOwnerId) {
        userService.findById(userOwnerId);
        List<Item> items =  itemRepository.findByOwnerOrderByIdAsc(userOwnerId);
        List<ItemResponse> responseItems = new ArrayList<>();

        Map<Item, List<Comment>> comments = commentRepository.findByItemId(userOwnerId)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        Map<Item, List<Booking>> bookings = bookingRepository.findByItemOwner(userOwnerId, SORT_START_DESC)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        for (Item item: items) {
            List<Comment> commentsById = comments.get(item);
            List<CommentResponse> commentResponses = commentMapper.toResponseCollection(commentsById);
            ItemResponse itemResponse = itemMapper.toResponse(item);
            itemResponse.setComments(commentResponses);
            toAssemble(bookings.get(item), itemResponse);
            responseItems.add(itemResponse);
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

    private void toAssemble(List<Booking> bookings, ItemResponse itemResponse) {

        if (bookings == null) {
            return;
        }

        bookings.stream()
                .filter(t -> t.getStart().isBefore(LocalDateTime.now()) && !t.getStatus().equals(StatusBooking.REJECTED))
                .max(Comparator.comparing(Booking::getEnd))
                .ifPresent(lastBooking -> itemResponse.setLastBooking(bookingMapper.toInformation(lastBooking, lastBooking.getBooker().getId())));
        bookings.stream()
                .filter(t -> t.getStart().isAfter(LocalDateTime.now()) && !t.getStatus().equals(StatusBooking.REJECTED))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(nextBooking -> itemResponse.setNextBooking(bookingMapper.toInformation(nextBooking, nextBooking.getBooker().getId())));

    }

    @Override
    public CommentResponse createComment(Long authorId, Long itemId, CommentCreateRequest request) {
        boolean completedBooking = bookingRepository.existsByItemIdAndBookerIdAndEndBefore(itemId, authorId, LocalDateTime.now());

        if (!completedBooking) {
            throw new ValidationException("Вы не можете оставить отзыв");
        }
        Comment comment = commentMapper.toComment(request);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));

        comment.setItem(item);
        User author = userService.findById(authorId);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        CommentResponse commentResponse = commentMapper.toResponse(comment);
        ItemResponse itemResponse = itemMapper.toResponse(item);
        commentResponse.setItemResponse(itemResponse);
        return commentResponse;
    }
}
