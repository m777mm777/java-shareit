package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.question.repository.QuestionJpaRepository;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    private User user;
    private UserResponse userResponse;
    private ItemCreateRequest itemCreateRequest;
    private Item item;
    private ItemResponse itemResponse;
    private Question question;
    private QuestionResponse questionResponse;
    private Comment comment;
    private CommentResponse commentResponse;
    private CommentCreateRequest commentCreateRequest;
    private static final Sort SORT_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private Pageable page = PageRequest.of(0, 10, Sort.by("id"));



    @Mock
    ItemMapper itemMapper;

    @Mock
    UserMapper userMapper;

    @Mock
    BookingMapper bookingMapper;

    @Mock
    CommentMapper commentMapper;

    @Mock
    ItemJpaRepository itemRepository;
    @Mock
    UserService userService;
    @Mock
    BookingJpaRepository bookingRepository;
    @Mock
    CommentJpaRepository commentRepository;
    @Mock
    QuestionJpaRepository questionJpaRepository;

    @InjectMocks
    ItemServiceImpl itemServiceImpl;

    @BeforeEach
    public void createObjects() {

        itemCreateRequest = new ItemCreateRequest("Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L);

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userResponse = new UserResponse(1L,
                "name",
                "mail1@mail.ru");

        question = new Question();
        question.setId(1L);
        question.setDescription("Любая отвертка нужна");
        question.setCreator(user);
        question.setCreated(LocalDateTime.now());

        item = new Item();
        item.setId(1L);
        item.setName("Отвертка");
        item.setDescription("Крестовая отвертка для саморезов");
        item.setAvailable(true);
        item.setOwner(user);
        item.setQuestion(question);

        commentResponse = new CommentResponse(1L,
                "Комментарий",
                itemResponse,
                "name",
                LocalDateTime.now());

        itemResponse = new ItemResponse(1L,
                "Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L,
                null,
                null,
                List.of(commentResponse),
                1L);

        commentResponse = new CommentResponse(1L,
                "Шикарная отвертка всем советую",
                itemResponse,
                "name",
                LocalDateTime.now());

        questionResponse = new QuestionResponse(1L,
                "Любая отвертка нужна",
                userResponse,
                LocalDateTime.now(),
                List.of(itemResponse));

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Шикарная отвертка всем советую");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.setText("Шикарная отвертка всем советую");
    }

    @Test
    public void createItemTest() {

        when(userService.findById(anyLong())).thenReturn(user);

        when(itemMapper.toItem(itemCreateRequest)).thenReturn(item);

        when(questionJpaRepository.getById(anyLong())).thenReturn(question);

        when(itemRepository.save(item)).thenReturn(item);

        Item item1 = itemServiceImpl.createItem(user.getId(), itemCreateRequest);

        assertEquals(1, item1.getId());
    }

    @Test
    public void updateTest() {

//        Item itemUpdate = item;
        Item itemUpdate = new Item();
        itemUpdate.setId(1L);

        itemUpdate.setOwner(user);
        itemUpdate.setQuestion(question);

        when(userService.findById(anyLong())).thenReturn(user);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(itemRepository.save(itemUpdate)).thenReturn(item);

        Item item1 = itemServiceImpl.update(user.getId(), item.getId(), itemUpdate);

        assertEquals("Отвертка", item1.getName());
    }

    @Test
    public void findByIdTest() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(userService.findById(anyLong())).thenReturn(user);

        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of(comment));

        when(commentMapper.toResponseCollection(List.of(comment))).thenReturn(List.of(commentResponse));

        when(itemMapper.toResponse(item)).thenReturn(itemResponse);

        when(bookingRepository.findByItemIdAndItemOwnerAndStatusNot(1L,
                user,
                StatusBooking.REJECTED))
                .thenReturn(new ArrayList<>());

        ItemResponse itemResponse = itemServiceImpl.findById(1L, 1L);

        assertEquals("Отвертка", itemResponse.getName());
    }

    @Test
    public void getAllItemsByOwnerTest() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Item> items = new ArrayList<>();
        items.add(item);
        Page<Item> itemsPage = new PageImpl<>(items);
        when(itemRepository.findByOwner(user, page)).thenReturn(itemsPage);

        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of(comment));

        when(bookingRepository.findByItemOwnerAndStatusNot(user,
                StatusBooking.REJECTED,
                SORT_START_DESC))
                .thenReturn(new ArrayList<>());

        when(commentMapper.toResponseCollection(List.of(comment))).thenReturn(List.of(commentResponse));

        when(itemMapper.toResponse(item)).thenReturn(itemResponse);

        List<ItemResponse> itemResponses = itemServiceImpl.getAllItemsByOwner(1L, 0, 10);

        assertEquals(1, itemResponses.size());
        assertEquals("Отвертка", itemResponses.get(0).getName());
    }

    @Test
    public void searchItemTest() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Item> items = new ArrayList<>();
        items.add(item);
        Page<Item> itemsPage = new PageImpl<>(items);
        when(itemRepository.searchItem("Крестовая", page)).thenReturn(itemsPage);

        List<Item> itemsReturn = itemServiceImpl.searchItem(1L,"Крестовая", 0, 10);

        assertEquals(1, itemsReturn.size());
        assertEquals("Отвертка", itemsReturn.get(0).getName());
    }

    @Test
    public void getItemByIdTest() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Item item1 = itemServiceImpl.getItemById(1L);

        assertEquals(1, item1.getId());
        assertEquals("Отвертка", item1.getName());
    }

    @Test
    public void createCommentTest() {

        boolean responseTrue = true;
        LocalDateTime time = LocalDateTime.now();
        when(bookingRepository.existsByItemIdAndBookerIdAndEndBefore(1L,
                        1L,
                        time))
                .thenReturn(true);

        when(commentMapper.toComment(commentCreateRequest)).thenReturn(comment);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(userService.findById(anyLong())).thenReturn(user);

        when(commentRepository.save(comment)).thenReturn(comment);

        when(commentMapper.toResponse(comment)).thenReturn(commentResponse);

        when(itemMapper.toResponse(item)).thenReturn(itemResponse);

        CommentResponse commentResponse1 = itemServiceImpl.createComment(1L, 1L, commentCreateRequest, time);
        assertEquals(1, commentResponse1.getId());
        assertEquals("Шикарная отвертка всем советую", commentResponse1.getText());
    }

}
