package com.carwashpro.backend.service;

import com.carwashpro.backend.constant.BookingStatus;
import com.carwashpro.backend.request.BookingRequest;
import com.carwashpro.backend.request.UpdateBookingStatusRequest;
import com.carwashpro.backend.response.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getMyBookings();

    BookingResponse getBookingById(Long bookingId);

    BookingResponse cancelBooking(Long bookingId);

    BookingResponse updateBookingStatus(
            Long bookingId,
            UpdateBookingStatusRequest request);

    Page<BookingResponse> getAllBookings(
            BookingStatus status,
            Pageable pageable
    );

}