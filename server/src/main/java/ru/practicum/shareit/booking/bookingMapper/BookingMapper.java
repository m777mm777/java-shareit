package ru.practicum.shareit.booking.bookingMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.controller.dto.InformationBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.controller.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public List<BookingResponse> toResponseCollection(List<Booking> bookings) {

        List<BookingResponse> bookingResponses = new ArrayList<>();

        for (Booking booking: bookings) {
            ItemResponse itemResponse = itemMapper.toResponse(booking.getItem());
            UserResponse userResponse = userMapper.toResponse(booking.getBooker());
            BookingResponse bookingResponse = toResponse(booking, itemResponse, userResponse);
            bookingResponses.add(bookingResponse);
        }
        return bookingResponses;
    }

    public Booking toBooking(BookingCreateRequest request) {
        if (request == null) {
            return null;
        } else {
            Booking booking = new Booking();
            booking.setStart(request.getStart());
            booking.setEnd(request.getEnd());
            return booking;
        }
    }

    public BookingResponse toResponse(Booking booking, ItemResponse itemResponse, UserResponse userResponse) {
        if (booking == null || itemResponse == null || userResponse == null) {
            return null;
        } else {
            BookingResponse.BookingResponseBuilder bookingResponse = BookingResponse.builder();

                bookingResponse.id(booking.getId());
                bookingResponse.start(booking.getStart());
                bookingResponse.end(booking.getEnd());
                bookingResponse.status(booking.getStatus().name());
                bookingResponse.item(itemResponse);bookingResponse.booker(userResponse);

                return bookingResponse.build();
        }
    }

    public InformationBooking toInformation(Booking booking, Long bookerId) {
        if (booking == null && bookerId == null) {
            return null;
        } else {
            InformationBooking informationBooking = new InformationBooking();
            if (booking != null) {
                informationBooking.setId(booking.getId());
                informationBooking.setStart(booking.getStart());
                informationBooking.setEnd(booking.getEnd());
                informationBooking.setStatus(booking.getStatus());
            }

            informationBooking.setBookerId(bookerId);
            return informationBooking;
        }
    }

}
