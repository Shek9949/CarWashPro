package com.carwashpro.backend.repository;

import com.carwashpro.backend.entity.ServiceCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCatalogRepository
        extends JpaRepository<ServiceCatalog, Long> {

    List<ServiceCatalog> findByActiveTrue();

    Optional<ServiceCatalog> findByIdAndActiveTrue(Long id);

    boolean existsByName(String name);
}