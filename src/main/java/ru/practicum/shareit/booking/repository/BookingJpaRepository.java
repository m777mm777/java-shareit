package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
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

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime now, LocalDateTime now2, Sort sort);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwner(Long ownerId, Sort sort);

    List<Booking> findByItemOwnerAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime now, LocalDateTime now2, Sort sort);

    List<Booking> findByItemOwnerAndEndBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerAndStartAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, StatusBooking status, Sort sort);

    List<Booking> findByItemOwnerAndStatus(Long ownerId, StatusBooking status, Sort sort);

    List<Booking> findByItemIdAndItemOwner(Long itemId, Long ownerId);

    List<Booking> findByItemIdAndBookerId(Long itemId, Long bookerId);
}
