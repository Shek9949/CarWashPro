package com.carwashpro.backend.response;

import com.carwashpro.backend.constant.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long id;

    private String vehicleNumber;

    private String serviceName;

    private LocalDateTime bookingDate;

    private BookingStatus status;
}