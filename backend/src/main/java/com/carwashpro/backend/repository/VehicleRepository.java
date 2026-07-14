package com.carwashpro.backend.repository;

import com.carwashpro.backend.constant.VehicleStatus;
import com.carwashpro.backend.entity.User;
import com.carwashpro.backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    List<Vehicle> findByCustomer(User customer);

    List<Vehicle> findByCustomerAndStatus(
            User customer,
            VehicleStatus status);
    Optional<Vehicle> findByIdAndCustomer(Long id, User customer);

    Optional<Vehicle> findByIdAndCustomerAndStatus(
            Long id,
            User customer,
            VehicleStatus status
    );

    boolean existsByVehicleNumber(String vehicleNumber);

    long countByCustomer(User customer);

}