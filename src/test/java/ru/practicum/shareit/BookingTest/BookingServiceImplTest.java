package ru.practicum.shareit.BookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.ResourceServerError;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

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
    private Booking booking;
    private BookingCreateRequest bookingCreateRequest;
    private LocalDateTime time = LocalDateTime.now();

    @Mock
    UserServiceImpl userService;

    @Mock
    ItemServiceImpl itemService;

    @Mock
    BookingMapper bookingMapper;

    @Mock
    BookingJpaRepository repository;

    @InjectMocks
    BookingServiceImpl bookingService;

    private static final Sort SORT_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private Pageable page = PageRequest.of(0, 10, SORT_START_DESC);

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
                time);

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
                time);

        questionResponse = new QuestionResponse(1L,
                "Любая отвертка нужна",
                userResponse,
                time,
                List.of(itemResponse));

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Шикарная отвертка всем советую");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(time);

        commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.setText("Шикарная отвертка всем советую");

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(time);
        booking.setEnd(time.plusSeconds(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(StatusBooking.WAITING);

        bookingCreateRequest = new BookingCreateRequest(1L, time, time.plusSeconds(1));
    }

    @Test
    public void createTest() {

        when(bookingMapper.toBooking(any(BookingCreateRequest.class))).thenReturn(booking);

        when(itemService.getItemById(anyLong())).thenReturn(item);

        when(userService.findById(anyLong())).thenReturn(user);

        when(repository.save(booking)).thenReturn(booking);

        Booking booking1 = bookingService.create(bookingCreateRequest, 2L);

        assertEquals(booking, booking1);
    }

    @Test
    public void updateStatusTest() {

        when(userService.findById(anyLong())).thenReturn(user);

        when(repository.findByIdAndItemOwner(anyLong(), any(User.class))).thenReturn(Optional.of(booking));

        when(repository.save(booking)).thenReturn(booking);

        Booking booking1 = bookingService.updateStatus(2L, 1L, true);

        assertEquals(booking, booking1);
    }

    @Test
    public void getStatusTest() {

        when(userService.findById(anyLong())).thenReturn(user);

        when(repository.findByIdAndBookerIdAndItemOwner(anyLong(), any(User.class))).thenReturn(Optional.of(booking));

        Booking booking1 = bookingService.getStatus(2L, 1L);

        assertEquals(booking, booking1);
    }

    @Test
    public void getBookingByItemTest() {

        when(repository.findByItemIdAndItemOwnerId(anyLong(), anyLong())).thenReturn(List.of(booking));

        List<Booking> booking1 = bookingService.getBookingByItem(2L, 1L);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByBookerTest_CURRENT() {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByBooker(2L, "CURRENT", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByBookerTest_PAST() {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByBooker(2L, "PAST", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByBookerTest_FUTURE() {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByBooker(2L, "FUTURE", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByBookerTest_WAITING() {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByBookerIdAndStatus(anyLong(), any(StatusBooking.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByBooker(2L, "WAITING", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByBookerTest_REJECTED() {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByBookerIdAndStatus(anyLong(), any(StatusBooking.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByBooker(2L, "REJECTED", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByBookerTest_ALL() {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByBookerId(anyLong(), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByBooker(2L, "ALL", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByBookerTest_Trow() {

        ResourceServerError exception = assertThrows(ResourceServerError.class, () -> {
            bookingService.getBookingsByBooker(2L, "UNKNOW", 0, 10);
        });

        assertEquals("Unknown state: UNKNOW", exception.getMessage());

    }

    @Test
    public void getBookingsByOwnerTest_CURRENT() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByItemOwnerAndStartBeforeAndEndAfter(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByOwner(2L, "CURRENT", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByOwnerTest_PAST() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByItemOwnerAndEndBefore(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByOwner(2L, "PAST", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByOwnerTest_FUTURE() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByItemOwnerAndStartAfter(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByOwner(2L, "FUTURE", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByOwnerTest_WAITING() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByItemOwnerAndStatus(any(User.class), any(StatusBooking.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByOwner(2L, "WAITING", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByOwnerTest_REJECTED() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByItemOwnerAndStatus(any(User.class), any(StatusBooking.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByOwner(2L, "REJECTED", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByOwnerTest_ALL() {

        when(userService.findById(anyLong())).thenReturn(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(repository.findByItemOwner(any(User.class), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> booking1 = bookingService.getBookingsByOwner(2L, "ALL", 0, 10);

        assertEquals(1, booking1.size());
    }

    @Test
    public void getBookingsByOwnerTest_Trow() {

        when(userService.findById(anyLong())).thenReturn(user);

        ResourceServerError exception = assertThrows(ResourceServerError.class, () -> {
            bookingService.getBookingsByBooker(2L, "UNKNOW", 0, 10);
        });

        assertEquals("Unknown state: UNKNOW", exception.getMessage());
    }
}
