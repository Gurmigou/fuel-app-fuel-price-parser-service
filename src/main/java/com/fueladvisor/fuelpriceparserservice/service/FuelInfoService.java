package com.fueladvisor.fuelpriceparserservice.service;

import com.fueladvisor.fuelpriceparserservice.model.dto.FuelInfoDto;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.repository.FuelInfoRepository;
import com.fueladvisor.fuelpriceparserservice.repository.GasStationRepository;
import com.fueladvisor.fuelpriceparserservice.repository.RegionRepository;
import com.fueladvisor.fuelpriceparserservice.repository.externalData.FuelDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
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

            regionRepository.saveAll(parsedResult.regions());
            gasStationRepository.saveAll(parsedResult.gasStations());
            fuelInfoRepository.saveAll(parsedResult.fuelInfoList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FuelInfoDto> getFuelInfosInRegion(String regionLatin) {
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