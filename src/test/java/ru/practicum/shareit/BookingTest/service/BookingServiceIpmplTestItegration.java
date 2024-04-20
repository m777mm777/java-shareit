package ru.practicum.shareit.BookingTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.controller.dto.ItemCreateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingServiceIpmplTestItegration {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BookingJpaRepository repository;

    @Autowired
    private BookingService bookingService;

    private static final Sort SORT_START_DESC = Sort.by(Sort.Direction.DESC, "start");

    @Test
    public void getBookingsByBookerTest() {

        LocalDateTime time = LocalDateTime.now();

        User user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");

        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("Отвертка",
                "Крестовая отвертка для саморезов",
                true,
                null);

        ItemCreateRequest itemCreateRequest2 = new ItemCreateRequest("Шуруповерт",
                "Шуруповерт аккумуляторный профессиональный",
                true,
                null);

        userService.create(user);

        User user1 = new User();
        user1.setName("name1");
        user1.setEmail("ff@mail.ru");

        userService.create(user1);

        Item item = itemService.createItem(1L, itemCreateRequest);
        itemService.createItem(1L, itemCreateRequest2);

        BookingCreateRequest bookingCreateRequest = new BookingCreateRequest(1L, time.minusMinutes(1), time.plusMinutes(1));

        bookingService.create(bookingCreateRequest, 2L);

        BookingCreateRequest bookingCreateRequest1 = new BookingCreateRequest(2L, time.minusMinutes(1), time.plusMinutes(1));

        bookingService.create(bookingCreateRequest1, 2L);

        List<Booking> bookings = bookingService.getBookingsByBooker(2L, "CURRENT", 0, 10);

        assertEquals(2, bookings.size());
        assertEquals("Отвертка", bookings.get(0).getItem().getName());
        assertEquals("Шуруповерт", bookings.get(1).getItem().getName());
    }
}
