package com.carwashpro.backend.service.impl;

import com.carwashpro.backend.constant.BookingStatus;
import com.carwashpro.backend.constant.VehicleStatus;
import com.carwashpro.backend.entity.Booking;
import com.carwashpro.backend.entity.ServiceCatalog;
import com.carwashpro.backend.entity.User;
import com.carwashpro.backend.entity.Vehicle;
import com.carwashpro.backend.exception.BusinessException;
import com.carwashpro.backend.exception.ResourceNotFoundException;
import com.carwashpro.backend.mapper.BookingMapper;
import com.carwashpro.backend.repository.BookingRepository;
import com.carwashpro.backend.repository.ServiceCatalogRepository;
import com.carwashpro.backend.repository.UserRepository;
import com.carwashpro.backend.repository.VehicleRepository;
import com.carwashpro.backend.request.BookingRequest;
import com.carwashpro.backend.request.UpdateBookingStatusRequest;
import com.carwashpro.backend.response.BookingResponse;
import com.carwashpro.backend.security.SecurityUtil;
import com.carwashpro.backend.service.BookingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceCatalogRepository serviceRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final SecurityUtil securityUtil;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            VehicleRepository vehicleRepository,
            ServiceCatalogRepository serviceRepository,
            UserRepository userRepository,
            BookingMapper bookingMapper,
            SecurityUtil securityUtil) {

        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
        this.securityUtil = securityUtil;
    }

    @Override
    public BookingResponse createBooking(BookingRequest request) {

        String email = securityUtil.getCurrentUserEmail();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository
                .findByIdAndCustomer(request.getVehicleId(), customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found"));

        if (!vehicle.getStatus().equals(VehicleStatus.ACTIVE)) {
            throw new ResourceNotFoundException("Vehicle is inactive");
        }

        ServiceCatalog service = serviceRepository
                .findByIdAndActiveTrue(request.getServiceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service not found"));

        if (request.getBookingDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException(
                    "Booking date cannot be in the past.");
        }

        boolean alreadyBooked =
                bookingRepository.existsByVehicleAndBookingDateAndStatusNot(
                        vehicle,
                        request.getBookingDate(),
                        BookingStatus.CANCELLED);

        if (alreadyBooked) {
            throw new BusinessException(
                    "Vehicle already has a booking for this time slot.");
        }

        Booking booking = Booking.builder()
                .customer(customer)
                .vehicle(vehicle)
                .service(service)
                .bookingDate(request.getBookingDate())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public List<BookingResponse> getMyBookings() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Booking> bookings =
                bookingRepository.findByCustomer_IdOrderByCreatedAtDesc(user.getId());

        return bookings.stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository
                .findByIdAndCustomer_Id(bookingId, customer.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse cancelBooking(Long bookingId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository
                .findByIdAndCustomer_Id(bookingId, customer.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BusinessException(
                    "Completed booking cannot be cancelled.");
        }

        if (booking.getStatus() == BookingStatus.IN_PROGRESS) {
            throw new BusinessException(
                    "Booking in progress cannot be cancelled.");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException(
                    "Booking is already cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        Booking updatedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(updatedBooking);
    }

    @Override
    public Page<BookingResponse> getAllBookings(
            BookingStatus status,
            Pageable pageable) {

        Page<Booking> bookings;

        if (status != null) {
            bookings = bookingRepository.findByStatus(status, pageable);
        } else {
            bookings = bookingRepository.findAll(pageable);
        }

        return bookings.map(bookingMapper::toResponse);
    }

    @Override
    public BookingResponse updateBookingStatus(
            Long bookingId,
            UpdateBookingStatusRequest request) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        BookingStatus currentStatus = booking.getStatus();
        BookingStatus newStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new BusinessException(
                    "Invalid booking status transition.");
        }

        booking.setStatus(newStatus);

        Booking updatedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(updatedBooking);
    }

    private boolean isValidStatusTransition(
            BookingStatus current,
            BookingStatus next) {

        return switch (current) {

            case PENDING ->
                    next == BookingStatus.CONFIRMED
                            || next == BookingStatus.CANCELLED;

            case CONFIRMED ->
                    next == BookingStatus.IN_PROGRESS
                            || next == BookingStatus.CANCELLED;

            case IN_PROGRESS ->
                    next == BookingStatus.COMPLETED;

            case COMPLETED, CANCELLED -> false;
        };
    }
}