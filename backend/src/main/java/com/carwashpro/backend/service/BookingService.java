package com.carwashpro.backend.service;

import com.carwashpro.backend.request.BookingRequest;
import com.carwashpro.backend.request.UpdateBookingStatusRequest;
import com.carwashpro.backend.response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getMyBookings();

    BookingResponse getBookingById(Long bookingId);

    BookingResponse cancelBooking(Long bookingId);

    List<BookingResponse> getAllBookings();

    BookingResponse updateBookingStatus(
            Long bookingId,
            UpdateBookingStatusRequest request);
}