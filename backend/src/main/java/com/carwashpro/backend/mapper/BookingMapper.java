package com.carwashpro.backend.mapper;

import com.carwashpro.backend.entity.Booking;
import com.carwashpro.backend.response.BookingResponse;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {

        return BookingResponse.builder()
                .id(booking.getId())
                .vehicleNumber(
                        booking.getVehicle().getVehicleNumber()
                )
                .serviceName(
                        booking.getService().getName()
                )
                .bookingDate(
                        booking.getBookingDate()
                )
                .status(
                        booking.getStatus()
                )
                .build();
    }
}