package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.State;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ResourceServerError;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
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
    @Transactional
    public Booking create(BookingCreateRequest bookingRequest, Long bookerId) {
        Booking booking = bookingMapper.toBooking(bookingRequest);
        Item item = itemService.getItemById(bookingRequest.getItemId());

        if (!item.getAvailable()) {
            throw new ValidationException("Бронь не доступна");
        }

        if (item.getOwner().getId().equals(bookerId)) {
            throw new ResourceNotFoundException("Нельзя бронировать свое");
        }

        booking.setBooker(userService.findById(bookerId));
        booking.setItem(item);
        booking.setStatus(StatusBooking.WAITING);
        return repository.save(booking);
    }

    @Override
    public Booking updateStatus(Long ownerId, Long bookingId, Boolean status) {
        User owner = userService.findById(ownerId);
        Booking booking = repository.findByIdAndItemOwner(bookingId, owner)
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
        User user = userService.findById(userId);
        return repository.findByIdAndBookerIdAndItemOwner(bookingId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Такого бронирования нет"));
    }

    @Override
    public List<Booking> getBookingByItem(Long itemId, Long ownerId) {
        return repository.findByItemIdAndItemOwnerId(itemId, ownerId);
    }

    @Override
    public List<Booking> getBookingsByBooker(Long bookerId, String status, Integer from, Integer size) {

        if (from < 0 || size < 0) {
            throw new ValidationException("Отрицательные значения страниц");
        }

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_START_DESC);
        userService.findById(bookerId);
        LocalDateTime time = LocalDateTime.now();
        Page<Booking> bookings;

        switch (checkState(status)) {
            case CURRENT:
                bookings = repository.findByBookerIdAndStartBeforeAndEndAfter(bookerId, time, time, page);
                break;
            case PAST:
                bookings = repository.findByBookerIdAndEndBefore(bookerId, time, page);
                break;
            case FUTURE:
                bookings = repository.findByBookerIdAndStartAfter(bookerId, time, page);
                break;
            case WAITING:
                bookings = repository.findByBookerIdAndStatus(bookerId, StatusBooking.WAITING, page);
                break;
            case REJECTED:
                bookings = repository.findByBookerIdAndStatus(bookerId, StatusBooking.REJECTED, page);
                break;
            case ALL:
                bookings = repository.findByBookerId(bookerId, page);
                break;
            default:
                throw new ResourceServerError("Unknown state: " + status);
        }
        return bookings.getContent();
    }

    @Override
    public List<Booking> getBookingsByOwner(Long ownerId, String status,Integer from, Integer size) {

        if (from < 0 || size < 0) {
            throw new ValidationException("Отрицательные значения страниц");
        }

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_START_DESC);
        User owner = userService.findById(ownerId);
        Page<Booking> bookings;
        LocalDateTime time = LocalDateTime.now();

        switch (checkState(status)) {
            case CURRENT:
                bookings = repository.findByItemOwnerAndStartBeforeAndEndAfter(owner, time, time, page);
                break;
            case PAST:
                bookings = repository.findByItemOwnerAndEndBefore(owner, time, page);
                break;
            case FUTURE:
                bookings = repository.findByItemOwnerAndStartAfter(owner, time, page);
                break;
            case WAITING:
                bookings = repository.findByItemOwnerAndStatus(owner, StatusBooking.WAITING, page);
                break;
            case REJECTED:
                bookings = repository.findByItemOwnerAndStatus(owner, StatusBooking.REJECTED, page);
                break;
            case ALL:
                bookings = repository.findByItemOwner(owner, page);
                break;
            default:
                throw new ResourceServerError("Unknown state: " + status);
        }
        return bookings.getContent();
    }

    private State checkState(String status) {
        State state;

        try {
            state = State.valueOf(status);
        } catch (Exception e) {
            throw new ResourceServerError("Unknown state: " + status);
        }
        return state;
    }

}