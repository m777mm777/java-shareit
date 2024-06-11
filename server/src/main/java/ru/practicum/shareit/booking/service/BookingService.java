package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking create(BookingCreateRequest booking, Long bookerId);

    Booking updateStatus(Long ownerId, Long bookingId, Boolean status);

    Booking getStatus(Long userId, Long bookingId);

    List<Booking> getBookingByItem(Long itemId, Long ownerId);

    List<Booking> getBookingsByBooker(Long bookerId, String status,Integer from, Integer size);

    List<Booking> getBookingsByOwner(Long ownerId, String status,Integer from, Integer size);
}
