package com.carwashpro.backend.service.impl;

import com.carwashpro.backend.constant.VehicleStatus;
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

        return vehicleRepository
                .findByCustomerAndStatus(customer, VehicleStatus.ACTIVE)
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
    public VehicleResponse updateVehicle(Long vehicleId,
                                         VehicleRequest request) {

        String email = securityUtil.getCurrentUserEmail();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository
                .findByIdAndCustomerAndStatus(
                        vehicleId,
                        customer,
                        VehicleStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Active vehicle not found"));

        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setColor(request.getColor());
        vehicle.setManufactureYear(request.getManufactureYear());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(updatedVehicle);
    }
    @Override
    public void deleteVehicle(Long vehicleId) {

        String email = securityUtil.getCurrentUserEmail();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository
                .findByIdAndCustomer(vehicleId, customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found"));

        boolean wasDefault = vehicle.getIsDefault();

        vehicle.setStatus(VehicleStatus.INACTIVE);
        vehicle.setIsDefault(false);

        vehicleRepository.save(vehicle);

        if (wasDefault) {

            List<Vehicle> activeVehicles =
                    vehicleRepository.findByCustomerAndStatus(
                            customer,
                            VehicleStatus.ACTIVE
                    );

            if (!activeVehicles.isEmpty()) {

                Vehicle newDefault = activeVehicles.get(0);

                newDefault.setIsDefault(true);

                vehicleRepository.save(newDefault);
            }
        }
    }

    @Override
    public VehicleResponse setDefaultVehicle(Long vehicleId) {

        String email = securityUtil.getCurrentUserEmail();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Vehicle selectedVehicle = vehicleRepository
                .findByIdAndCustomerAndStatus(
                        vehicleId,
                        customer,
                        VehicleStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active vehicle not found"
                        ));
        List<Vehicle> vehicles =
                vehicleRepository.findByCustomer(customer);

        for (Vehicle vehicle : vehicles) {
            vehicle.setIsDefault(false);
        }

        selectedVehicle.setIsDefault(true);

        vehicleRepository.saveAll(vehicles);

        return vehicleMapper.toResponse(selectedVehicle);
    }
}