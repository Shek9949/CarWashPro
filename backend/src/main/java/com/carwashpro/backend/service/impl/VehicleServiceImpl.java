package com.carwashpro.backend.service.impl;

import com.carwashpro.backend.mapper.VehicleMapper;
import com.carwashpro.backend.repository.VehicleRepository;
import com.carwashpro.backend.repository.UserRepository;
import com.carwashpro.backend.request.VehicleRequest;
import com.carwashpro.backend.response.VehicleResponse;
import com.carwashpro.backend.service.VehicleService;
import org.springframework.stereotype.Service;
import com.carwashpro.backend.entity.User;
import com.carwashpro.backend.entity.Vehicle;
import com.carwashpro.backend.exception.DuplicateResourceException;
import com.carwashpro.backend.exception.ResourceNotFoundException;
import com.carwashpro.backend.security.SecurityUtil;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            VehicleMapper vehicleMapper,
            UserRepository userRepository,
            SecurityUtil securityUtil) {

        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
    }


    @Override
    public VehicleResponse addVehicle(VehicleRequest request) {

        String email = securityUtil.getCurrentUserEmail();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (vehicleRepository.existsByVehicleNumber(
                request.getVehicleNumber().toUpperCase())) {

            throw new DuplicateResourceException(
                    "Vehicle number already exists");
        }

        Vehicle vehicle = vehicleMapper.toEntity(request);

        vehicle.setCustomer(customer);

        vehicle.setVehicleNumber(
                request.getVehicleNumber().toUpperCase()
        );

        if (vehicleRepository.countByCustomer(customer) == 0) {
            vehicle.setIsDefault(true);
        } else {
            vehicle.setIsDefault(false);
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(savedVehicle);
    }
    @Override
    public List<VehicleResponse> getMyVehicles() {

        String email = securityUtil.getCurrentUserEmail();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        return vehicleRepository.findByCustomer(customer)
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Override
    public VehicleResponse getVehicleById(Long vehicleId) {

        String email = securityUtil.getCurrentUserEmail();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository
                .findByIdAndCustomer(vehicleId, customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found"));

        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    public VehicleResponse updateVehicle(Long vehicleId, VehicleRequest request) {
        return null;
    }

    @Override
    public void deleteVehicle(Long vehicleId) {

    }

    @Override
    public VehicleResponse setDefaultVehicle(Long vehicleId) {
        return null;
    }
}