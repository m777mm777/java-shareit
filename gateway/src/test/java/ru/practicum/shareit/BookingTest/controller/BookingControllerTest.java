package ru.practicum.shareit.BookingTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.validate.ValidateBooking;
import ru.practicum.shareit.constants.Constants;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;


    @MockBean
    private ValidateBooking validateBooking;

    @MockBean
    private BookingClient bookingClient;

    LocalDateTime time = LocalDateTime.now();
    BookingCreateRequest bookingCreateRequest;
    BookingCreateRequest bookingCreateRequestBad;
    ResponseEntity<Object> response;

    @BeforeEach
    public void startTest() {
        bookingCreateRequest = new BookingCreateRequest(1L, time.plusMinutes(1), time.plusMinutes(2));
        bookingCreateRequestBad = new BookingCreateRequest(1L, time.minusMinutes(1), time.plusMinutes(2));

        response = ResponseEntity.noContent().build();
    }


    @Test
    public void createTest() throws Exception {

        when(validateBooking.validate(any(BookingCreateRequest.class))).thenReturn(true);
        when(bookingClient.crateBooking(anyLong(), any(BookingCreateRequest.class))).thenReturn(response);

        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createTestNotVaid() throws Exception {

        when(validateBooking.validate(bookingCreateRequestBad)).thenReturn(false);

        when(bookingClient.crateBooking(anyLong(), any(BookingCreateRequest.class))).thenReturn(response);


        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingCreateRequestBad))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStatusTest() throws Exception {

        Long bookingId = 1L;
        Boolean status = true;

        when(bookingClient.updateStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(response);


        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", status.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getStatusTest() throws Exception {

        Long bookingId = 1L;

        when(bookingClient.getStatus(anyLong(), anyLong())).thenReturn(response);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getBookingsByBookerTest() throws Exception {

        String status = "CURRENT";
        Integer from = 0;
        Integer size = 10;

        when(bookingClient.getBookingsByBooker(anyLong(), any(BookingState.class), anyInt(), anyInt())).thenReturn(response);

        mvc.perform(get("/bookings")
                        .param("state", status.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getBookingsByBookerTestNotValid() throws Exception {

        String status = "CURRENT";
        Integer from = -1;
        Integer size = 10;

        when(bookingClient.getBookingsByBooker(anyLong(), any(BookingState.class), anyInt(), anyInt())).thenReturn(response);

        mvc.perform(get("/bookings")
                        .param("state", status.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getBookingsByOwnerItemsTest() throws Exception {

        String status = "CURRENT";
        Integer from = 0;
        Integer size = 10;

        when(bookingClient.getBookingsByOwnerItems(anyLong(), any(BookingState.class), anyInt(), anyInt())).thenReturn(response);

        mvc.perform(get("/bookings/owner")
                        .param("state", status.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getBookingsByOwnerItemsTestNotValid() throws Exception {

        String status = "CURRENT";
        Integer from = 0;
        Integer size = 0;

        when(bookingClient.getBookingsByOwnerItems(anyLong(), any(BookingState.class), anyInt(), anyInt())).thenReturn(response);

        mvc.perform(get("/bookings/owner")
                        .param("state", status.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .content(objectMapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header(Constants.RESPONSEHEADER, 1L))
                .andExpect(status().isBadRequest());
    }

}
