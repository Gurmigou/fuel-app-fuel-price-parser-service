package com.fueladvisor.fuelpriceparserservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FuelPriceDto {
    private String fuelType;
    private Double price;
}
