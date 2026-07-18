package com.carwashpro.backend.controller;

import com.carwashpro.backend.request.ServiceCatalogRequest;
import com.carwashpro.backend.response.ServiceCatalogResponse;
import com.carwashpro.backend.service.ServiceCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceCatalogController {

    private final ServiceCatalogService serviceCatalogService;

    public ServiceCatalogController(
            ServiceCatalogService serviceCatalogService) {

        this.serviceCatalogService = serviceCatalogService;
    }

    @PostMapping
    public ResponseEntity<ServiceCatalogResponse> createService(
            @Valid @RequestBody ServiceCatalogRequest request) {

        ServiceCatalogResponse response =
                serviceCatalogService.createService(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServiceCatalogResponse>> getAllServices() {

        return ResponseEntity.ok(
                serviceCatalogService.getAllServices()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCatalogResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceCatalogRequest request) {

        return ResponseEntity.ok(
                serviceCatalogService.updateService(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable Long id) {

        serviceCatalogService.deleteService(id);

        return ResponseEntity.noContent().build();
    }
}