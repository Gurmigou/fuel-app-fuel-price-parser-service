package com.fueladvisor.fuelpriceparserservice.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class FuelInfoDto {
    private String gasStationId;
    private String gasStationName;
    private String region;
    private List<FuelPriceDto> fuelPrices;
}