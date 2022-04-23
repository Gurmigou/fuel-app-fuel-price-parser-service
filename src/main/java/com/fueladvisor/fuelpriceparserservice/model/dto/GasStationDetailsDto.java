package com.fueladvisor.fuelpriceparserservice.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GasStationDetailsDto {
    private String gasStationId;
    private String gasStationName;
    private String email;
    private String phoneNumber;
    private List<FuelPriceDto> averagePriceList;
}
