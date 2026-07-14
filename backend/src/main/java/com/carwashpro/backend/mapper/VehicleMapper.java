package com.carwashpro.backend.mapper;

import com.carwashpro.backend.entity.Vehicle;
import com.carwashpro.backend.request.VehicleRequest;
import com.carwashpro.backend.response.VehicleResponse;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequest request) {

        return Vehicle.builder()
                .vehicleNumber(request.getVehicleNumber())
                .brand(request.getBrand())
                .model(request.getModel())
                .vehicleType(request.getVehicleType())
                .fuelType(request.getFuelType())
                .color(request.getColor())
                .manufactureYear(request.getManufactureYear())
                .build();
    }

    public VehicleResponse toResponse(Vehicle vehicle) {

        return VehicleResponse.builder()
                .id(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .vehicleType(vehicle.getVehicleType())
                .fuelType(vehicle.getFuelType())
                .color(vehicle.getColor())
                .manufactureYear(vehicle.getManufactureYear())
                .isDefault(vehicle.getIsDefault())
                .status(vehicle.getStatus())
                .build();
    }
}