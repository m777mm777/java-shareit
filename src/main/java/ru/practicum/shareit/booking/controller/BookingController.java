package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.bookingMapper.BookingMapper;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validate.ValidateBooking;
import ru.practicum.shareit.item.controller.constants.Constants;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final ValidateBooking validateBooking;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @PostMapping
    public BookingResponse crateBooking(@RequestHeader(Constants.RESPONSEHEADER) Long bookerId,
                                        @RequestBody @Valid BookingCreateRequest request) {

        validateBooking.validate(request);
        log.info("CrateBooking bookerId {} request {}", bookerId, request);
        Booking booking = bookingService.create(request, bookerId);
        ItemResponse itemResponse = itemMapper.toResponse(booking.getItem());
        UserResponse userResponse = userMapper.toResponse(booking.getBooker());
        return bookingMapper.toResponse(booking, itemResponse, userResponse);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse updateStatus(@RequestHeader(Constants.RESPONSEHEADER) Long ownerId,
                                                       @PathVariable("bookingId") Long bookingId,
                                                       @RequestParam("approved") Boolean status) {
        log.info("UpdateStatus ownerId {} bookingId {} status {}", ownerId, bookingId, status);
        Booking booking = bookingService.updateStatus(ownerId, bookingId, status);
        ItemResponse itemResponse = itemMapper.toResponse(booking.getItem());
        UserResponse userResponse = userMapper.toResponse(booking.getBooker());
        return bookingMapper.toResponse(booking, itemResponse, userResponse);
    }

    @GetMapping("{bookingId}")
    public BookingResponse getStatus(@RequestHeader(Constants.RESPONSEHEADER) Long userId,
                                                    @PathVariable("bookingId") Long bookingId) {
        log.info("GetStatus userId {} bookingId {}", userId, bookingId);

        Booking booking = bookingService.getStatus(userId, bookingId);
        ItemResponse itemResponse = itemMapper.toResponse(booking.getItem());
        UserResponse userResponse = userMapper.toResponse(booking.getBooker());
        return bookingMapper.toResponse(booking, itemResponse, userResponse);
    }

    @GetMapping
    public List<BookingResponse> getBookingsByBooker(@RequestHeader(Constants.RESPONSEHEADER) Long bookerId,
                                             @RequestParam(value = "state", defaultValue = "ALL") String status) {
        log.info("GetBookingsByBooker bookerId {} status {}", bookerId, status);
        return bookingMapper.toResponseCollection(bookingService.getBookingsByBooker(bookerId, status));
    }

    @GetMapping("/owner")
    public List<BookingResponse> getBookingsByOwnerItems(@RequestHeader(Constants.RESPONSEHEADER) Long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String status) {
        log.info("GetBookingsByOwnerItems ownerId {} status {}", ownerId, status);
        return bookingMapper.toResponseCollection(bookingService.getBookingsByOwner(ownerId, status));
    }
}