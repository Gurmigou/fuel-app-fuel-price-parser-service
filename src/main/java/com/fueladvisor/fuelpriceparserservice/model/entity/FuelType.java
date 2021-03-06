package com.fueladvisor.fuelpriceparserservice.model.entity;

import lombok.Getter;

@Getter
public enum FuelType {
    A95_PLUS("А95+"),
    A95("А95"),
    A92("А92"),
    DIESEL_FUEL("ДТ"),
    DIESEL_FUEL_PLUS("ДТ+"),
    GAS("Газ");

    private final String name;

    FuelType(String name) {
        this.name = name;
    }
}
