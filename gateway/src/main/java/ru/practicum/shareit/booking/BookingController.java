package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.validate.ValidateBooking;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final ValidateBooking validateBooking;
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> crateBooking(@RequestHeader(Constants.RESPONSEHEADER) Long bookerId,
                                               @RequestBody @Valid BookingCreateRequest request) {
        validateBooking.validate(request);
        log.info("CrateBooking bookerId {} request {}", bookerId, request);
        return bookingClient.crateBooking(bookerId, request);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(Constants.RESPONSEHEADER) Long ownerId,
                                                       @PathVariable("bookingId") Long bookingId,
                                                       @RequestParam("approved") Boolean status) {
        log.info("UpdateStatus ownerId {} bookingId {} status {}", ownerId, bookingId, status);
        return bookingClient.updateStatus(ownerId, bookingId, status);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getStatus(@RequestHeader(Constants.RESPONSEHEADER) Long userId,
                                                    @PathVariable("bookingId") Long bookingId) {
        log.info("GetStatus userId {} bookingId {}", userId, bookingId);
        return bookingClient.getStatus(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(@RequestHeader(Constants.RESPONSEHEADER) Long bookerId,
                                             @RequestParam(value = "state", defaultValue = "ALL") String status,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GetBookingsByBooker bookerId {} status {} from {} size {}", bookerId, status, from, size);
        BookingState state = BookingState.from(status)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + status));
        return bookingClient.getBookingsByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwnerItems(@RequestHeader(Constants.RESPONSEHEADER) Long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String status,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GetBookingsByOwnerItems ownerId {} status {} from {} size {}", ownerId, status, from, size);
        BookingState state = BookingState.from(status)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + status));
        return bookingClient.getBookingsByOwnerItems(ownerId, state, from, size);
    }
}