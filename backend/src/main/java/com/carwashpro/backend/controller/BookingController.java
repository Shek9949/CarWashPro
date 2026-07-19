package com.carwashpro.backend.controller;

import com.carwashpro.backend.constant.BookingStatus;
import com.carwashpro.backend.request.BookingRequest;
import com.carwashpro.backend.response.BookingResponse;
import com.carwashpro.backend.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carwashpro.backend.request.UpdateBookingStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request) {

        BookingResponse response =
                bookingService.createBooking(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {

        return ResponseEntity.ok(
                bookingService.getMyBookings()
        );
    }
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                bookingService.getBookingById(bookingId)
        );
    }
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(bookingId)
        );
    }
    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getAllBookings(

            @RequestParam(required = false)
            BookingStatus status,

            Pageable pageable) {

        return ResponseEntity.ok(
                bookingService.getAllBookings(status, pageable)
        );
    }


    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long bookingId,
            @Valid @RequestBody UpdateBookingStatusRequest request) {

        return ResponseEntity.ok(
                bookingService.updateBookingStatus(bookingId, request)
        );
    }
}