package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.constants.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndItemOwner(Long bookingId, User ownerId);

    @Query("select b from Booking b " +
            "inner join b.item i " +
            "inner join b.booker u " +
            "where b.id = ?1 and (i.owner = ?2 or u.id = ?2)")
    Optional<Booking> findByIdAndBookerIdAndItemOwner(Long bookingId, User userId);

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime now, LocalDateTime now2, Sort sort);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerAndStatusNot(User owner, StatusBooking state, Sort sort);

    List<Booking> findByItemOwner(User ownerId, Sort sort);

    List<Booking> findByItemOwnerAndStartBeforeAndEndAfter(User bookerId, LocalDateTime now, LocalDateTime now2, Sort sort);

    List<Booking> findByItemOwnerAndEndBefore(User bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerAndStartAfter(User bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, StatusBooking status, Sort sort);

    List<Booking> findByItemOwnerAndStatus(User ownerId, StatusBooking status, Sort sort);

    List<Booking> findByItemIdAndItemOwnerAndStatusNot(Long itemId, User owner, StatusBooking state);

    List<Booking> findByItemIdAndItemOwner(Long itemId, Long ownerId);

    boolean existsByItemIdAndBookerIdAndEndBefore(Long itemId, Long authorId, LocalDateTime now);
}
