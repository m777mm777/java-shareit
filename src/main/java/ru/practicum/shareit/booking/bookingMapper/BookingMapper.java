package ru.practicum.shareit.booking.bookingMapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.controller.dto.InformationBooking;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toBooking(BookingCreateRequest request);

    BookingResponse toResponse(Booking booking);

    InformationBooking toInformation(Booking booking, Long bookerId);

    List<BookingResponse> toResponseCollection(List<Booking> bookings);

}
