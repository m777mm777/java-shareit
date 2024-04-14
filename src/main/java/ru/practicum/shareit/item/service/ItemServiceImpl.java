package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.question.repository.QuestionJpaRepository;
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
    private final QuestionJpaRepository questionJpaRepository;
    private static final Sort SORT_START_DESC = Sort.by(Sort.Direction.DESC, "start");

    @Override
    @Transactional
    public Item createItem(Long userOwnerId, ItemCreateRequest itemCreateRequest) {
        User user = userService.findById(userOwnerId);
        Item item = itemMapper.toItem(itemCreateRequest);

        if (itemCreateRequest.getRequestId() != null) {
            item.setQuestion(questionJpaRepository.getById(itemCreateRequest.getRequestId()));
        }

        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long userOwnerId, Long itemId, Item item) {
        User user = userService.findById(userOwnerId);
        item.setId(itemId);
        item.setOwner(user);
        Item itemOld = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));

        if (!userOwnerId.equals(itemOld.getOwner().getId())) {
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

        User owner = userService.findById(ownerId);

        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentResponse> commentResponses = commentMapper.toResponseCollection(comments);

        ItemResponse itemResponse = itemMapper.toResponse(item);
        itemResponse.setComments(commentResponses);

        List<Booking> bookings = bookingRepository.findByItemIdAndItemOwnerAndStatusNot(itemId, owner, StatusBooking.REJECTED);

        toAssemble(bookings, itemResponse, LocalDateTime.now());

        return itemResponse;
    }

    @Override
    public List<ItemResponse> getAllItemsByOwner(Long userOwnerId, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("Отрицательные значения страниц");
        }

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("id"));

        User owner = userService.findById(userOwnerId);

        Page<Item> items =  itemRepository.findByOwner(owner, page);
        List<ItemResponse> responseItems = new ArrayList<>();

        Map<Item, List<Comment>> comments = commentRepository.findByItemId(userOwnerId)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        Map<Item, List<Booking>> bookings = bookingRepository.findByItemOwnerAndStatusNot(owner, StatusBooking.REJECTED, SORT_START_DESC)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        for (Item item: items) {
            List<Comment> commentsById = comments.get(item);
            List<CommentResponse> commentResponses = commentMapper.toResponseCollection(commentsById);
            ItemResponse itemResponse = itemMapper.toResponse(item);
            itemResponse.setComments(commentResponses);
            toAssemble(bookings.get(item), itemResponse, LocalDateTime.now());
            responseItems.add(itemResponse);
        }
        return responseItems;
    }

    @Override
    public List<Item> searchItem(Long userId, String text, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("Отрицательные значения страниц");
        }

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("id"));

        userService.findById(userId);

        if (text.isBlank()) {
            return List.of();
        }

        Page<Item> itemsPage = itemRepository.searchItem(text, page);
        List<Item> items = itemsPage.getContent();
        return items;
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item по такому id нет"));
    }

    private void toAssemble(List<Booking> bookings, ItemResponse itemResponse, LocalDateTime now) {

        if (bookings == null) {
            return;
        }

        bookings.stream()
                .filter(t -> !t.getStart().isAfter(now))
                .max(Comparator.comparing(Booking::getEnd))
                .ifPresent(lastBooking -> itemResponse.setLastBooking(bookingMapper.toInformation(lastBooking, lastBooking.getBooker().getId())));
        bookings.stream()
                .filter(t -> t.getStart().isAfter(now))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(nextBooking -> itemResponse.setNextBooking(bookingMapper.toInformation(nextBooking, nextBooking.getBooker().getId())));

    }

    @Override
    public CommentResponse createComment(Long authorId, Long itemId, CommentCreateRequest request, LocalDateTime now) {

        boolean completedBooking = bookingRepository.existsByItemIdAndBookerIdAndEndBefore(itemId, authorId, now);

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
