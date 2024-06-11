package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Booking> findByBookerId(Long bookerId, Pageable sort);

    Page<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime now, LocalDateTime now2, Pageable sort);

    Page<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime now, Pageable sort);

    Page<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime now, Pageable sort);

    List<Booking> findByItemIdInAndStatusNot(List<Long> itemIds, StatusBooking state, Sort sort);

    Page<Booking> findByItemOwner(User ownerId, Pageable sort);

    Page<Booking> findByItemOwnerAndStartBeforeAndEndAfter(User bookerId, LocalDateTime now, LocalDateTime now2, Pageable sort);

    Page<Booking> findByItemOwnerAndEndBefore(User bookerId, LocalDateTime now, Pageable sort);

    Page<Booking> findByItemOwnerAndStartAfter(User bookerId, LocalDateTime now, Pageable sort);

    Page<Booking> findByBookerIdAndStatus(Long bookerId, StatusBooking status, Pageable sort);

    Page<Booking> findByItemOwnerAndStatus(User ownerId, StatusBooking status, Pageable sort);

    List<Booking> findByItemIdAndItemOwnerAndStatusNot(Long itemId, User owner, StatusBooking state);

    List<Booking> findByItemIdAndItemOwnerId(Long itemId, Long ownerId);

    boolean existsByItemIdAndBookerIdAndEndBefore(Long itemId, Long authorId, LocalDateTime now);
}
