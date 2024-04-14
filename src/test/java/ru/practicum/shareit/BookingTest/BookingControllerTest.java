package ru.practicum.shareit.BookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validate.ValidateBooking;
import ru.practicum.shareit.item.controller.constants.Constants;
import ru.practicum.shareit.item.controller.dto.CommentCreateRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.question.controller.dto.QuestionResponse;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private BookingMapper bookingMapper;

    @MockBean
    private ValidateBooking validateBooking;

    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private UserMapper userMapper;

    LocalDateTime time = LocalDateTime.now();
    BookingCreateRequest bookingCreateRequest;
    User user;
    Item item;
    Booking booking;
    BookingResponse bookingResponse;
    ItemCreateRequest itemCreateRequest;
    UserResponse userResponse;
    Question question;
    QuestionResponse questionResponse;
    CommentResponse commentResponse;
    ItemResponse itemResponse;
    CommentCreateRequest commentCreateRequest;
    Comment comment;

    @BeforeEach
    public void startTest() {
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
        booking.setStart(time.plusMinutes(1));
        booking.setEnd(time.plusMinutes(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(StatusBooking.WAITING);

        bookingCreateRequest = new BookingCreateRequest(1L, time.plusMinutes(1), time.plusMinutes(2));

        bookingResponse = new BookingResponse(1L, time.plusMinutes(1), time.plusMinutes(2), itemResponse, userResponse, "WAITING");
    }


    @Test
    public void createTest() throws Exception {

        when(validateBooking.validate(any(BookingCreateRequest.class))).thenReturn(true);
        when(bookingService.create(any(BookingCreateRequest.class), anyLong())).thenReturn(booking);

        when(itemMapper.toResponse(any(Item.class))).thenReturn(itemResponse);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);
        when(bookingMapper.toResponse(any(Booking.class), any(ItemResponse.class), any(UserResponse.class))).thenReturn(bookingResponse);

        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingCreateRequest.getItemId()), Long.class));
    }

    @Test
    public void updateStatusTest() throws Exception {

        Long bookingId = 1L;
        Boolean status = true;

        when(bookingService.updateStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        when(itemMapper.toResponse(any(Item.class))).thenReturn(itemResponse);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);
        when(bookingMapper.toResponse(any(Booking.class), any(ItemResponse.class), any(UserResponse.class))).thenReturn(bookingResponse);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", status.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getStatusTest() throws Exception {

        Long bookingId = 1L;

        when(bookingService.getStatus(anyLong(), anyLong())).thenReturn(booking);

        when(itemMapper.toResponse(any(Item.class))).thenReturn(itemResponse);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);
        when(bookingMapper.toResponse(any(Booking.class), any(ItemResponse.class), any(UserResponse.class))).thenReturn(bookingResponse);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getBookingsByBookerTest() throws Exception {

        String status = "CURRENT";
        Integer from = 0;
        Integer size = 10;

        when(bookingService.getBookingsByBooker(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(booking));

        when(bookingMapper.toResponseCollection(anyList())).thenReturn(List.of(bookingResponse));

        mvc.perform(get("/bookings")
                        .param("state", status.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getBookingsByOwnerItemsTest() throws Exception {

        String status = "CURRENT";
        Integer from = 0;
        Integer size = 10;

        when(bookingService.getBookingsByOwner(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(booking));

        when(bookingMapper.toResponseCollection(anyList())).thenReturn(List.of(bookingResponse));

        mvc.perform(get("/bookings/owner")
                        .param("state", status.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isOk());
    }

}
