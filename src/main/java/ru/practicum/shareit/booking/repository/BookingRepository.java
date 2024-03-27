package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    Booking create(Booking booking);

    Booking updateStatus(Booking booking, Boolean status);

    Optional<Booking> getBookingByBookingIdAndOwnerId(Long bookingId, Long ownerId);

    Optional<Booking> getStatus(Long userId, Long bookingId);

    List<Booking> getBookingsByBooker(Long bookerId, String status);

    List<Booking> getBookingsByOwner(Long ownerId, String status);

    List<Booking> findByItemIdAndItemOwner(Long itemId, Long ownerId);

    List<Booking> findByItemIdAndBookerId(Long itemId, Long bookerId);
}
