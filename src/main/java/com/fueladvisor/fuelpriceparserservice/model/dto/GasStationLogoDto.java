package com.fueladvisor.fuelpriceparserservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GasStationLogoDto {
    private String gasStationId;
    private byte[] img;
    private final String logoContentType = "image/jpg";
    private boolean exists;
}
