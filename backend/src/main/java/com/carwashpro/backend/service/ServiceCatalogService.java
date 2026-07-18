package com.carwashpro.backend.service;

import com.carwashpro.backend.request.ServiceCatalogRequest;
import com.carwashpro.backend.response.ServiceCatalogResponse;

import java.util.List;

public interface ServiceCatalogService {

    ServiceCatalogResponse createService(
            ServiceCatalogRequest request);

    List<ServiceCatalogResponse> getAllServices();

    ServiceCatalogResponse getServiceById(Long serviceId);

    ServiceCatalogResponse updateService(
            Long serviceId,
            ServiceCatalogRequest request);

    void deleteService(Long serviceId);
}