package com.fueladvisor.fuelpriceparserservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class ScheduledParserService {
    private final FuelInfoService fuelInfoService;

    @Autowired
    public ScheduledParserService(FuelInfoService fuelInfoService) {
        this.fuelInfoService = fuelInfoService;
    }

    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.DAYS)
    public void scheduledParseFuelData() {
        try {
            fuelInfoService.updateFuelData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
