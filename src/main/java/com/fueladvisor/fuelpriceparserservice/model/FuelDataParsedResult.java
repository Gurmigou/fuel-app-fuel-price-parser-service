package com.fueladvisor.fuelpriceparserservice.model;

import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.entity.GasStation;
import com.fueladvisor.fuelpriceparserservice.model.entity.Region;

import java.util.List;

public record FuelDataParsedResult(
        List<FuelInfo> fuelInfoList,
        Iterable<Region> regions,
        Iterable<GasStation> gasStations) {
}
