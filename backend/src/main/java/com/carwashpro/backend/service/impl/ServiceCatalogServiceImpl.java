package com.carwashpro.backend.service.impl;

import com.carwashpro.backend.entity.ServiceCatalog;
import com.carwashpro.backend.exception.DuplicateResourceException;
import com.carwashpro.backend.exception.ResourceNotFoundException;
import com.carwashpro.backend.mapper.ServiceCatalogMapper;
import com.carwashpro.backend.repository.ServiceCatalogRepository;
import com.carwashpro.backend.request.ServiceCatalogRequest;
import com.carwashpro.backend.response.ServiceCatalogResponse;
import com.carwashpro.backend.service.ServiceCatalogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCatalogServiceImpl
        implements ServiceCatalogService {

    private final ServiceCatalogRepository serviceRepository;
    private final ServiceCatalogMapper serviceMapper;

    public ServiceCatalogServiceImpl(
            ServiceCatalogRepository serviceRepository,
            ServiceCatalogMapper serviceMapper) {

        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public ServiceCatalogResponse createService(
            ServiceCatalogRequest request) {

        if (serviceRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Service already exists");
        }

        ServiceCatalog service =
                serviceMapper.toEntity(request);

        ServiceCatalog savedService =
                serviceRepository.save(service);

        return serviceMapper.toResponse(savedService);
    }

    @Override
    public List<ServiceCatalogResponse> getAllServices() {

        return serviceRepository
                .findByActiveTrue()
                .stream()
                .map(serviceMapper::toResponse)
                .toList();
    }

    @Override
    public ServiceCatalogResponse getServiceById(Long serviceId) {
        return null;
    }



    @Override
    public void deleteService(Long serviceId) {

        ServiceCatalog service = serviceRepository
                .findByIdAndActiveTrue(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service not found"));

        service.setActive(false);

        serviceRepository.save(service);
    }
    @Override
    public ServiceCatalogResponse updateService(
            Long serviceId,
            ServiceCatalogRequest request) {

        ServiceCatalog service = serviceRepository
                .findByIdAndActiveTrue(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service not found"));

        if (!service.getName().equalsIgnoreCase(request.getName())
                && serviceRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Service already exists");
        }

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setEstimatedDuration(request.getEstimatedDuration());

        ServiceCatalog updatedService =
                serviceRepository.save(service);

        return serviceMapper.toResponse(updatedService);
    }

}