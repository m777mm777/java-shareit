package ru.practicum.shareit.questionTest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
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
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.question.controller.dto.QuestionCreateRequest;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.mapper.QuestionMapper;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.question.repository.QuestionJpaRepository;
import ru.practicum.shareit.question.service.QuestionServiceImpl;
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
public class QuestionServiceImplTest {

    private User user;
    private UserResponse userResponse;
    private ItemCreateRequest itemCreateRequest;
    private Item item;
    private ItemResponse itemResponse;
    private Question question;
    private QuestionResponse questionResponse;
    private QuestionCreateRequest questionCreateRequest;
    private Comment comment;
    private CommentResponse commentResponse;
    private CommentCreateRequest commentCreateRequest;
    private static final Sort SORT_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");
    private Pageable page = PageRequest.of(0, 10, Sort.by("id"));



    @Mock
    ItemMapper itemMapper;

    @Mock
    UserMapper userMapper;

    @Mock
    BookingMapper bookingMapper;
    @Mock
    QuestionMapper questionMapper;
    @Mock
    CommentMapper commentMapper;
    @Mock
    ItemService itemService;
    @Mock
    ItemJpaRepository itemRepository;
    @Mock
    UserService userService;
    @Mock
    BookingJpaRepository bookingRepository;
    @Mock
    CommentJpaRepository commentRepository;
    @Mock
    QuestionJpaRepository repository;

    @InjectMocks
    QuestionServiceImpl questionService;

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

        questionCreateRequest = new QuestionCreateRequest();
        questionCreateRequest.setDescription("Любая отвертка нужна");

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
    public void createQuestionTest() {

        when(userService.findById(anyLong())).thenReturn(user);
        when(questionMapper.toQuestion(any(QuestionCreateRequest.class))).thenReturn(question);
        when(repository.save(question)).thenReturn(question);

        Question question1 = questionService.createQuestion(1L, questionCreateRequest);

        assertEquals(1, question1.getId());
    }

    @Test
    public void getAllQuestionByCreatorTest() {

        when(userService.findById(anyLong())).thenReturn(user);
        when(repository.findByCreatorId(anyLong(), any(Sort.class))).thenReturn(List.of(question));
        when(questionMapper.toResponseCollection(anyList())).thenReturn(List.of(questionResponse));
        when(itemRepository.findAllByQuestionIn(anyList())).thenReturn(List.of(item));
        when(itemMapper.toResponseCollection(anyList())).thenReturn(List.of(itemResponse));

        List<QuestionResponse> questions = questionService.getAllQuestionByCreator(1L);

        assertEquals(1, questions.size());
        assertEquals("Любая отвертка нужна", questions.get(0).getDescription());
    }

    @Test
    public void getAllQuestionOtherUserTest() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Question> questions = new ArrayList<>();
        questions.add(question);
        Page<Question> questionPage = new PageImpl<>(questions);
        when(repository.findByCreatorIdNot(anyLong(), any(Pageable.class))).thenReturn(questionPage);
        when(itemRepository.findAllByQuestionIn(anyList())).thenReturn(List.of(item));

        List<QuestionResponse> questions1 = questionService.getAllQuestionOtherUser(1L, 0, 10);

        assertEquals(1, questions.size());
        assertEquals("Любая отвертка нужна", questions.get(0).getDescription());
    }

    @Test
    public void getQuestionByIdTest() {

        when(userService.findById(anyLong())).thenReturn(user);
        when(repository.findById(anyLong())).thenReturn(Optional.of(question));
        when(questionMapper.toResponse(any(Question.class))).thenReturn(questionResponse);
        when(itemRepository.findByQuestionId(anyLong())).thenReturn(List.of(item));
        when(itemMapper.toResponseCollection(anyList())).thenReturn(List.of(itemResponse));

        QuestionResponse questionResponse1 = questionService.getQuestionById(1L, 1L);

        assertEquals(1, questionResponse1.getId());
        assertEquals("Любая отвертка нужна", questionResponse1.getDescription());
    }
}
