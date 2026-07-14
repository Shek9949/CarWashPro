package com.carwashpro.backend.request;

import com.carwashpro.backend.constant.FuelType;
import com.carwashpro.backend.constant.VehicleType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleRequest {

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Manufacture year is required")
    @Min(value = 1980, message = "Invalid manufacture year")
    @Max(value = 2100, message = "Invalid manufacture year")
    private Integer manufactureYear;
}