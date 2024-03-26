package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ResourceServerError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {
    private final BookingJpaRepository bookingJpaRepository;

    @Override
    public Booking create(Booking booking) {
        return bookingJpaRepository.save(booking);
    }

    @Override
    public Booking updateStatus(Booking booking, Boolean status) {

        if (status.equals(true)) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }

        return bookingJpaRepository.save(booking);
    }

    @Override
    public Optional<Booking> getStatus(Long userId, Long bookingId) {
        return bookingJpaRepository.findByIdAndBookerIdAndItemOwner(bookingId, userId);
    }

    @Override
    public List<Booking> getBookingsByBooker(Long bookerId, String status) {
        List<Booking> bookings;

        switch (status) {
            case "CURRENT":
                bookings = bookingJpaRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookings = bookingJpaRepository.findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingJpaRepository.findByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingJpaRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, StatusBooking.WAITING);
                break;
            case "REJECTED":
                bookings = bookingJpaRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, StatusBooking.REJECTED);
                break;
            case "ALL":
                bookings = bookingJpaRepository.findByBookerIdOrderByStartDesc(bookerId);
                break;
            default:
                throw new ResourceServerError("Unknown state: " + status);
        }

        return bookings;
    }

    @Override
    public List<Booking> getBookingsByOwner(Long ownerId, String status) {
        List<Booking> bookings;

        switch (status) {
            case "CURRENT":
                bookings = bookingJpaRepository.findByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookings = bookingJpaRepository.findByItemOwnerAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingJpaRepository.findByItemOwnerAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingJpaRepository.findByItemOwnerAndStatusOrderByStartDesc(ownerId, StatusBooking.WAITING);
                break;
            case "REJECTED":
                bookings = bookingJpaRepository.findByItemOwnerAndStatusOrderByStartDesc(ownerId, StatusBooking.REJECTED);
                break;
            case "ALL":
                bookings = bookingJpaRepository.findByItemOwnerOrderByStartDesc(ownerId);
                break;
            default:
                throw new ResourceServerError("Unknown state: " + status);
        }
        return bookings;
    }

    @Override
    public Optional<Booking> getBookingByBookingIdAndOwnerId(Long bookingId, Long ownerId) {
        return bookingJpaRepository.findByIdAndItemOwner(bookingId, ownerId);
    }

    @Override
    public List<Booking> findByItemIdAndItemOwner(Long itemId, Long ownerId) {
        return bookingJpaRepository.findByItemIdAndItemOwner(itemId, ownerId);
    }

    @Override
    public List<Booking> findByItemIdAndBookerId(Long itemId, Long bookerId) {
        return bookingJpaRepository.findByItemIdAndBookerId(itemId, bookerId);
    }
}
