package com.fueladvisor.fuelpriceparserservice.service;

import com.fueladvisor.fuelpriceparserservice.model.FuelDataParsedResult;
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
import java.util.*;
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
    public Optional<Integer> updateFuelData() throws IOException {
        System.out.println(1);
        FuelDataParsedResult parsedResult = fuelDataParser.parseFuelData();
        System.out.println(2);

        Iterable<Region> regions = regionRepository.findAll();

        System.out.println(3);

        if (!regions.iterator().hasNext()) {
            System.out.println(4);
            regionRepository.saveAll(parsedResult.getRegions());
            System.out.println(5);
        } else {
            System.out.println(6);
            Map<String, Region> regionsMap = new HashMap<>();
            regions.forEach(region -> regionsMap.put(region.getLatinName(), region));

            System.out.println(7);

            parsedResult.getRegions()
                    .forEach(parsedRegion -> {
                        Region foundRegion = regionsMap.get(parsedRegion.getLatinName());
                        parsedRegion.setId(foundRegion.getId());
                    });

            System.out.println(8);
        }

        Iterable<GasStation> gasStations = gasStationRepository.findAll();

        System.out.println(9);

        if (!gasStations.iterator().hasNext()) {
            System.out.println(10);
            gasStationRepository.saveAll(parsedResult.getGasStations());
            System.out.println(11);
        } else {
            System.out.println(12);
            Map<String, GasStation> gasStationsMap = new HashMap<>();
            gasStations.forEach(gasStation -> gasStationsMap.put(gasStation.getName(), gasStation));

            System.out.println(13);

            parsedResult.getGasStations()
                    .forEach(parsedGasStation -> {
                        GasStation foundGasStation = gasStationsMap.get(parsedGasStation.getName());
                        parsedGasStation.setId(foundGasStation.getId());
                    });

            System.out.println(14);
        }

        fuelInfoRepository.saveAll(parsedResult.getFuelInfoList());

        System.out.println(15);

        return Optional.of(parsedResult.getFuelInfoList().size());
    }

    @Transactional
    public List<FuelInfoDto> getFuelInfosInRegion(String regionLatin) {
        if (Objects.equals(regionLatin, "Kyiv City"))
            regionLatin = "Kyivs'ka oblast";

        List<FuelInfo> fuelInfos = fuelInfoRepository.getFuelInfosByRegionLatinName(regionLatin);

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