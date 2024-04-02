package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ResourceServerError;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;
    private final BookingJpaRepository repository;
    private static final Sort SORT_START_DESC = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public Booking create(BookingCreateRequest bookingRequest, Long bookerId) {
        Booking booking = bookingMapper.toBooking(bookingRequest);
        Item item = itemService.getItemById(bookingRequest.getItemId());

        if (!item.getAvailable()) {
            throw new ValidationException("Бронь не доступна");
        }

        if (item.getOwner().equals(bookerId)) {
            throw new ResourceNotFoundException("Нельзя бронировать свое");
        }

        booking.setBooker(userService.findById(bookerId));
        booking.setItem(item);
        booking.setStatus(StatusBooking.WAITING);
        return repository.save(booking);
    }

    @Override
    public Booking updateStatus(Long ownerId, Long bookingId, Boolean status) {
        Booking booking = repository.findByIdAndItemOwner(bookingId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Такого бронирования нет"));

        if (booking.getStatus() == StatusBooking.APPROVED) {
            throw new ValidationException("Бронирование подтверждено ранее");
        }

        if (status.equals(true)) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }

        return repository.save(booking);
    }

    @Override
    public Booking getStatus(Long userId, Long bookingId) {
        return repository.findByIdAndBookerIdAndItemOwner(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Такого бронирования нет"));
    }

    @Override
    public List<Booking> getBookingByItem(Long itemId, Long ownerId) {
        return repository.findByItemIdAndItemOwner(itemId, ownerId);
    }

    @Override
    public List<Booking> getBookingsByBooker(Long bookerId, String status) {
        userService.findById(bookerId);

        List<Booking> bookings;

        switch (status) {
            case "CURRENT":
                bookings = repository.findByBookerIdAndStartBeforeAndEndAfter(bookerId, LocalDateTime.now(), LocalDateTime.now(), SORT_START_DESC);
                break;
            case "PAST":
                bookings = repository.findByBookerIdAndEndBefore(bookerId, LocalDateTime.now(), SORT_START_DESC);
                break;
            case "FUTURE":
                bookings = repository.findByBookerIdAndStartAfter(bookerId, LocalDateTime.now(), SORT_START_DESC);
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatus(bookerId, StatusBooking.WAITING, SORT_START_DESC);
                break;
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatus(bookerId, StatusBooking.REJECTED, SORT_START_DESC);
                break;
            case "ALL":
                bookings = repository.findByBookerId(bookerId, SORT_START_DESC);
                break;
            default:
                throw new ResourceServerError("Unknown state: " + status);
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByOwner(Long ownerId, String status) {
        userService.findById(ownerId);
        List<Booking> bookings;

        switch (status) {
            case "CURRENT":
                bookings = repository.findByItemOwnerAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now(), LocalDateTime.now(), SORT_START_DESC);
                break;
            case "PAST":
                bookings = repository.findByItemOwnerAndEndBefore(ownerId, LocalDateTime.now(), SORT_START_DESC);
                break;
            case "FUTURE":
                bookings = repository.findByItemOwnerAndStartAfter(ownerId, LocalDateTime.now(), SORT_START_DESC);
                break;
            case "WAITING":
                bookings = repository.findByItemOwnerAndStatus(ownerId, StatusBooking.WAITING, SORT_START_DESC);
                break;
            case "REJECTED":
                bookings = repository.findByItemOwnerAndStatus(ownerId, StatusBooking.REJECTED, SORT_START_DESC);
                break;
            case "ALL":
                bookings = repository.findByItemOwner(ownerId, SORT_START_DESC);
                break;
            default:
                throw new ResourceServerError("Unknown state: " + status);
        }
        return bookings;
    }
}
