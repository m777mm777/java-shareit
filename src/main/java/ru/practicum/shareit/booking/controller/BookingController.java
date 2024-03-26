package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validate.ValidateBooking;
import ru.practicum.shareit.item.controller.constants.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final ValidateBooking validateBooking;

    @PostMapping
    public BookingResponse crateBooking(@RequestHeader(Constants.RESPONSEHEADER) @NotNull Long bookerId,
                                        @RequestBody @NotNull @Valid BookingCreateRequest request) {

        validateBooking.validate(request);
        log.info("CrateBooking bookerId {} request {}", bookerId, request);
        return bookingMapper.toResponse(bookingService.create(request, bookerId));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse updateStatus(@RequestHeader(Constants.RESPONSEHEADER) @NotNull Long ownerId,
                                                       @PathVariable("bookingId") Long bookingId,
                                                       @RequestParam("approved") @NotNull Boolean status) {
        log.info("UpdateStatus ownerId {} bookingId {} status {}", ownerId, bookingId, status);
        return bookingMapper.toResponse(bookingService.updateStatus(ownerId, bookingId, status));
    }

    @GetMapping("{bookingId}")
    public BookingResponse getStatus(@RequestHeader(Constants.RESPONSEHEADER) @NotNull Long userId,
                                                    @PathVariable("bookingId") Long bookingId) {
        log.info("GetStatus userId {} bookingId {}", userId, bookingId);
        return bookingMapper.toResponse(bookingService.getStatus(userId, bookingId));
    }

    @GetMapping
    public List<BookingResponse> getBookingsByBooker(@RequestHeader(Constants.RESPONSEHEADER) @NotNull Long bookerId,
                                             @RequestParam(value = "state", defaultValue = "ALL") String status) {
        log.info("GetBookingsByBooker bookerId {} status {}", bookerId, status);
        return bookingMapper.toResponseCollection(bookingService.getBookingsByBooker(bookerId, status));
    }

    @GetMapping("/owner")
    public List<BookingResponse> getBookingsByOwnerItems(@RequestHeader(Constants.RESPONSEHEADER) @NotNull Long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String status) {
        log.info("GetBookingsByOwnerItems ownerId {} status {}", ownerId, status);
        return bookingMapper.toResponseCollection(bookingService.getBookingsByOwner(ownerId, status));
    }
}