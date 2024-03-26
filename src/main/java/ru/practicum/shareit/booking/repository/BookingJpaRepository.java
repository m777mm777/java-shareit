package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndItemOwner(Long bookingId, Long ownerId);

    @Query("select b from Booking b " +
            "inner join b.item i " +
            "inner join b.booker u " +
            "where b.id = ?1 and (i.owner = ?2 or u.id = ?2)")
    Optional<Booking> findByIdAndBookerIdAndItemOwner(Long bookingId, Long userId);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findByItemOwnerOrderByStartDesc(Long ownerId);

    List<Booking> findByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findByItemOwnerAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findByItemOwnerAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, StatusBooking status);

    List<Booking> findByItemOwnerAndStatusOrderByStartDesc(Long ownerId, StatusBooking status);

    List<Booking> findByItemIdAndItemOwner(Long itemId, Long ownerId);

    List<Booking> findByItemIdAndBookerId(Long itemId, Long bookerId);
}
