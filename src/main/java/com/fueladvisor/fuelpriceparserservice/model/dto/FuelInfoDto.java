package com.fueladvisor.fuelpriceparserservice.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class FuelInfoDto {
    private Integer id;
    private String region;
    private String fuelType;
    private String gasStation;
    private Double price;

    private byte[] logo;
    private final String logoContentType = "image/jpg";
}