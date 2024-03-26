package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;
    private final BookingRepository repository;

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
        return repository.create(booking);
    }

    @Override
    public Booking updateStatus(Long ownerId, Long bookingId, Boolean status) {
        Booking booking = repository.getBookingByBookingIdAndOwnerId(bookingId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Такого бронирования нет"));

        if (booking.getStatus() == StatusBooking.APPROVED) {
            throw new ValidationException("Бронирование подтверждено ранее");
        }

        return repository.updateStatus(booking, status);
    }

    @Override
    public Booking getStatus(Long userId, Long bookingId) {
        return repository.getStatus(userId, bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Такого бронирования нет"));
    }

    @Override
    public List<Booking> getBookingByItem(Long itemId, Long ownerId) {
        return repository.findByItemIdAndItemOwner(itemId, ownerId);
    }

    @Override
    public List<Booking> getBookingsByBooker(Long bookerId, String status) {
        userService.findById(bookerId);
        return repository.getBookingsByBooker(bookerId, status);
    }

    @Override
    public List<Booking> getBookingsByOwner(Long ownerId, String status) {
        userService.findById(ownerId);
        return repository.getBookingsByOwner(ownerId, status);
    }
}
