package com.fueladvisor.fuelpriceparserservice.model;

import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.entity.GasStation;
import com.fueladvisor.fuelpriceparserservice.model.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FuelDataParsedResult {
    private List<FuelInfo> fuelInfoList;
    private Iterable<Region> regions;
    private Iterable<GasStation> gasStations ;
}