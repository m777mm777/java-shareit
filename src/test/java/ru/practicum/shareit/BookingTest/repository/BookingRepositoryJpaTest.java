package ru.practicum.shareit.BookingTest.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.question.repository.QuestionJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingRepositoryJpaTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    BookingJpaRepository bookingJpaRepository;

    private User user;
    private Item item;
    private Question question;
    private Booking booking;
    private Pageable page = PageRequest.of(0, 10, Sort.by("id"));
    private static final Sort SORT_START_DESC = Sort.by(Sort.Direction.DESC, "start");

    @BeforeEach
    public void createObjects() {

        user = new User();
        user.setName("name");
        user.setEmail("mail1@mail.ru");
        user = userJpaRepository.save(user);

        question = new Question();
        question.setDescription("Любая отвертка нужна");
        question.setCreator(user);
        question.setCreated(LocalDateTime.now());
        question = questionJpaRepository.save(question);

        item = new Item();
        item.setName("Отвертка");
        item.setDescription("Крестовая отвертка для саморезов");
        item.setAvailable(true);
        item.setOwner(user);
        item.setQuestion(question);
        item = itemJpaRepository.save(item);

        booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusMinutes(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(StatusBooking.WAITING);
        bookingJpaRepository.save(booking);
    }

    @Test
    @Order(1)
    public void findByIdAndItemOwnerTest() {

       Optional<Booking> bookingOptional = bookingJpaRepository.findByIdAndItemOwner(1L, user);
       Booking booking1 = bookingOptional.get();

        assertEquals(1, booking1.getId());
        assertEquals(item, booking1.getItem());
    }

    @Test
    @Order(2)
    public void findByIdAndBookerIdAndItemOwnerTest() {

        Optional<Booking> bookingOptional = bookingJpaRepository.findByIdAndBookerIdAndItemOwner(booking.getId(), user);
        Booking booking1 = bookingOptional.get();

        assertEquals(2, booking1.getId());
        assertEquals(item, booking1.getItem());
    }

    @Test
    @Order(3)
    public void findByBookerIdTest() {

       List<Booking> bookings = bookingJpaRepository.findByBookerId(user.getId(), page).getContent();

       assertEquals(1, bookings.size());
       assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(4)
    public void findByBookerIdAndStartBeforeAndEndAfterTest() {

        LocalDateTime time = LocalDateTime.now();

        List<Booking> bookings = bookingJpaRepository.findByBookerIdAndStartBeforeAndEndAfter(user.getId(), time, time, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(5)
    public void findByBookerIdAndEndBeforeTest() {

        LocalDateTime time = LocalDateTime.now();
        booking.setStart(time.minusMinutes(2));
        booking.setEnd(time.minusMinutes(1));
        bookingJpaRepository.save(booking);

        List<Booking> bookings = bookingJpaRepository.findByBookerIdAndEndBefore(user.getId(), time, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(6)
    public void findByBookerIdAndStartAfterTest() {

        LocalDateTime time = LocalDateTime.now();
        booking.setStart(time.plusMinutes(1));
        booking.setEnd(time.plusMinutes(2));
        bookingJpaRepository.save(booking);

        List<Booking> bookings = bookingJpaRepository.findByBookerIdAndStartAfter(user.getId(), time, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(7)
    public void findByItemOwnerAndStatusNotTest() {

        List<Booking> bookings = bookingJpaRepository.findByItemOwnerAndStatusNot(user, StatusBooking.REJECTED, SORT_START_DESC);

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(8)
    public void findByItemOwnerTest() {

        List<Booking> bookings = bookingJpaRepository.findByItemOwner(user, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(9)
    public void findByItemOwnerAndStartBeforeAndEndAfterTest() {

        LocalDateTime time = LocalDateTime.now();

        List<Booking> bookings = bookingJpaRepository.findByItemOwnerAndStartBeforeAndEndAfter(user, time, time, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(10)
    public void findByItemOwnerAndEndBeforeTest() {

        LocalDateTime time = LocalDateTime.now().plusMinutes(1);

        List<Booking> bookings = bookingJpaRepository.findByItemOwnerAndEndBefore(user, time, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(11)
    public void findByItemOwnerAndStartAfterTest() {

        LocalDateTime time = LocalDateTime.now().minusMinutes(1);

        List<Booking> bookings = bookingJpaRepository.findByItemOwnerAndStartAfter(user, time, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(12)
    public void findByBookerIdAndStatusTest() {

        List<Booking> bookings = bookingJpaRepository.findByBookerIdAndStatus(user.getId(), StatusBooking.WAITING, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(12)
    public void findByItemOwnerAndStatusTest() {

        List<Booking> bookings = bookingJpaRepository.findByItemOwnerAndStatus(user, StatusBooking.WAITING, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(13)
    public void findByItemIdAndItemOwnerAndStatusNotTest() {

        List<Booking> bookings = bookingJpaRepository.findByItemIdAndItemOwnerAndStatusNot(item.getId(), user, StatusBooking.REJECTED);

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(14)
    public void findByItemIdAndItemOwnerTest() {

        List<Booking> bookings = bookingJpaRepository.findByItemIdAndItemOwnerId(item.getId(), user.getId());

        assertEquals(1, bookings.size());
        assertEquals(item, bookings.get(0).getItem());
    }

    @Test
    @Order(15)
    public void existsByItemIdAndBookerIdAndEndBeforeTest() {

        LocalDateTime time = LocalDateTime.now().plusMinutes(1);

        boolean isBefore = bookingJpaRepository.existsByItemIdAndBookerIdAndEndBefore(item.getId(), user.getId(), time);

        assertEquals(true, isBefore);
    }
}
