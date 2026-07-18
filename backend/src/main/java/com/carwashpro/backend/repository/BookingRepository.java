package com.carwashpro.backend.repository;

import com.carwashpro.backend.constant.BookingStatus;
import com.carwashpro.backend.entity.Booking;
import com.carwashpro.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer(User customer);

    List<Booking> findByCustomerAndStatus(
            User customer,
            BookingStatus status
    );

    List<Booking> findByCustomer_IdOrderByCreatedAtDesc(Long customerId);
    Optional<Booking> findByIdAndCustomer_Id(Long bookingId, Long customerId);

}