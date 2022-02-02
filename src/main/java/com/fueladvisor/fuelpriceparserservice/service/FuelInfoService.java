package com.fueladvisor.fuelpriceparserservice.service;

import com.fueladvisor.fuelpriceparserservice.model.FuelDataParsedResult;
import com.fueladvisor.fuelpriceparserservice.repository.FuelDataParser;
import com.fueladvisor.fuelpriceparserservice.repository.FuelInfoRepository;
import com.fueladvisor.fuelpriceparserservice.repository.GasStationRepository;
import com.fueladvisor.fuelpriceparserservice.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    public void updateFuelData() {
        try {
            FuelDataParsedResult parsedResult = fuelDataParser.parseFuelData();

            regionRepository.saveAll(parsedResult.regions());
            gasStationRepository.saveAll(parsedResult.gasStations());
            fuelInfoRepository.saveAll(parsedResult.fuelInfoList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}