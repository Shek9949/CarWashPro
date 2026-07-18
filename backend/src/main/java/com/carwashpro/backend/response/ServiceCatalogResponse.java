package com.carwashpro.backend.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCatalogResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer estimatedDuration;

    private Boolean active;
}