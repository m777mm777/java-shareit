package ru.practicum.shareit.BookingTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.controller.dto.InformationBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BookingMapperTest {

    private ItemMapper itemMapper = new ItemMapper();

    private UserMapper userMapper = new UserMapper();

    private BookingMapper bookingMapper = new BookingMapper(itemMapper, userMapper);

    Booking booking;
    BookingCreateRequest bookingCreateRequest;
    BookingResponse bookingResponse;
    User user;
    UserResponse userResponse;
    Item item;
    ItemResponse itemResponse;
    LocalDateTime time;

    @BeforeEach
    public void create() {

        time = LocalDateTime.now();

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        userResponse = new UserResponse(1L, "name", "test@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("Отвертка");
        item.setDescription("Крестовая отвертка для саморезов");
        item.setAvailable(true);
        item.setOwner(user);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(time);
        booking.setEnd(time.plusSeconds(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(StatusBooking.WAITING);

        itemResponse = new ItemResponse(1L,
                "Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                1L,
                null,
                null,
                new ArrayList<>(),
                1L);

        bookingResponse = new BookingResponse(1L, time, time, itemResponse,
                userResponse,
                "WAITING");

        bookingCreateRequest = new BookingCreateRequest(1L, time, time.plusSeconds(1));
    }

    @Test
    public void toResponseCollectionTest() {

       List<BookingResponse> bookingResponses = bookingMapper.toResponseCollection(List.of(booking));

        assertEquals(1, bookingResponses.size());
    }

    @Test
    public void toBookingTest() {

        Booking booking1 = bookingMapper.toBooking(bookingCreateRequest);

        assertEquals(time, booking1.getStart());
    }

    @Test
    public void toBookingTestNegative() {

        Booking booking1 = bookingMapper.toBooking(null);

        assertEquals(null, booking1);
    }

    @Test
    public void toResponseTest() {

        BookingResponse bookingResponse1 = bookingMapper.toResponse(booking, itemResponse, userResponse);

        assertEquals(1, bookingResponse1.getId());
        assertEquals("Отвертка", bookingResponse1.getItem().getName());
        assertEquals("name", bookingResponse1.getBooker().getName());
    }

    @Test
    public void toResponseTestNegative() {

        BookingResponse bookingResponse1 = bookingMapper.toResponse(booking, null, userResponse);

        assertEquals(null, bookingResponse1);
    }

    @Test
    public void toInformationTest() {

        InformationBooking informationBooking = bookingMapper.toInformation(booking, 1L);

        assertEquals(1, informationBooking.getBookerId());
    }
}
