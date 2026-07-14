package com.carwashpro.backend.response;

import com.carwashpro.backend.constant.FuelType;
import com.carwashpro.backend.constant.VehicleStatus;
import com.carwashpro.backend.constant.VehicleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {

    private Long id;

    private String vehicleNumber;

    private String brand;

    private String model;

    private VehicleType vehicleType;

    private FuelType fuelType;

    private String color;

    private Integer manufactureYear;

    private Boolean isDefault;

    private VehicleStatus status;
}