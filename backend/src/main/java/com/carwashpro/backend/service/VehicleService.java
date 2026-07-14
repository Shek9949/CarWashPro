package com.carwashpro.backend.service;

import com.carwashpro.backend.response.VehicleResponse;
import com.carwashpro.backend.request.VehicleRequest;

import java.util.List;

public interface VehicleService {

    VehicleResponse addVehicle(VehicleRequest request);

    List<VehicleResponse> getMyVehicles();

    VehicleResponse getVehicleById(Long vehicleId);

    VehicleResponse updateVehicle(Long vehicleId, VehicleRequest request);

    void deleteVehicle(Long vehicleId);

    VehicleResponse setDefaultVehicle(Long vehicleId);

}