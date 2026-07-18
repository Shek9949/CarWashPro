package com.carwashpro.backend.mapper;

import com.carwashpro.backend.entity.ServiceCatalog;
import com.carwashpro.backend.request.ServiceCatalogRequest;
import com.carwashpro.backend.response.ServiceCatalogResponse;
import org.springframework.stereotype.Component;

@Component
public class ServiceCatalogMapper {

    public ServiceCatalog toEntity(ServiceCatalogRequest request) {

        return ServiceCatalog.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .estimatedDuration(request.getEstimatedDuration())
                .build();
    }

    public ServiceCatalogResponse toResponse(ServiceCatalog service) {

        return ServiceCatalogResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .estimatedDuration(service.getEstimatedDuration())
                .active(service.getActive())
                .build();
    }
}