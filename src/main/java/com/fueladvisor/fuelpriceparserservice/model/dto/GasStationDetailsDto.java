package com.fueladvisor.fuelpriceparserservice.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GasStationDetailsDto {
    private String gasStationId;
    private String email;
    private String phoneNumber;
    private Double averageFuelPrice;
}
