package ru.practicum.shareit.booking.bookingMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.controller.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.controller.dto.InformationBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.user.controller.dto.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toBooking(BookingCreateRequest request);

    @Mapping(target = "item", expression = "java(itemResponse)")
    @Mapping(target = "booker", expression = "java(userResponse)")
    @Mapping(target = "id", source = "booking.id")
    BookingResponse toResponse(Booking booking, ItemResponse itemResponse, UserResponse userResponse);

    InformationBooking toInformation(Booking booking, Long bookerId);

    List<BookingResponse> toResponseCollection(List<Booking> bookings);

}
