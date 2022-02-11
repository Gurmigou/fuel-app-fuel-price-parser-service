package com.fueladvisor.fuelpriceparserservice.service;

import com.fueladvisor.fuelpriceparserservice.model.dto.FuelInfoDto;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.entity.GasStation;
import com.fueladvisor.fuelpriceparserservice.model.entity.Region;
import com.fueladvisor.fuelpriceparserservice.repository.FuelInfoRepository;
import com.fueladvisor.fuelpriceparserservice.repository.GasStationRepository;
import com.fueladvisor.fuelpriceparserservice.repository.RegionRepository;
import com.fueladvisor.fuelpriceparserservice.repository.externalData.FuelDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FuelInfoService {
    private final FuelDataParser fuelDataParser;
    private final FuelInfoRepository fuelInfoRepository;
    private final GasStationRepository gasStationRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public FuelInfoService(FuelDataParser fuelDataParser,
                           FuelInfoRepository fuelInfoRepository,
                           GasStationRepository gasStationRepository,
                           RegionRepository regionRepository) {
        this.fuelDataParser = fuelDataParser;
        this.fuelInfoRepository = fuelInfoRepository;
        this.gasStationRepository = gasStationRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional
    public void updateFuelData() {
        try {
            var parsedResult = fuelDataParser.parseFuelData();

            Iterable<Region> regions = regionRepository.findAll();
            if (!regions.iterator().hasNext()) {
                regionRepository.saveAll(parsedResult.regions());
            } else {
                var regionsMap = new HashMap<String, Region>();
                regions.forEach(region -> regionsMap.put(region.getLatinName(), region));

                parsedResult.regions()
                        .forEach(parsedRegion -> {
                            Region foundRegion = regionsMap.get(parsedRegion.getLatinName());
                            parsedRegion.setId(foundRegion.getId());
                        });
            }

            Iterable<GasStation> gasStations = gasStationRepository.findAll();
            if (!gasStations.iterator().hasNext()) {
                gasStationRepository.saveAll(parsedResult.gasStations());
            } else {
                var gasStationsMap = new HashMap<String, GasStation>();
                gasStations.forEach(gasStation -> gasStationsMap.put(gasStation.getName(), gasStation));

                parsedResult.gasStations()
                        .forEach(parsedGasStation -> {
                            GasStation foundGasStation = gasStationsMap.get(parsedGasStation.getName());
                            parsedGasStation.setId(foundGasStation.getId());
                        });
            }

            fuelInfoRepository.saveAll(parsedResult.fuelInfoList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FuelInfoDto> getFuelInfosInRegion(String regionLatin) {
        if (Objects.equals(regionLatin, "Kyiv City"))
            regionLatin = "Kyivs'ka oblast";

        var fuelInfos = fuelInfoRepository.getFuelInfosByRegionLatinName(regionLatin);

        return fuelInfos.stream()
                .map(this::mapToFuelInfoDto)
                .collect(Collectors.toList());
    }

    private FuelInfoDto mapToFuelInfoDto(FuelInfo fuelInfo) {
        return FuelInfoDto.builder()
                .fuelType(fuelInfo.getFuelType().getName())
                .region(fuelInfo.getRegion().getName())
                .gasStation(fuelInfo.getGasStation().getName())
                .price(fuelInfo.getPrice())
                .build();
    }
}