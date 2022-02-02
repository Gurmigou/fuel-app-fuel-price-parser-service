package com.fueladvisor.fuelpriceparserservice.controller;

import com.fueladvisor.fuelpriceparserservice.service.FuelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fuel-info")
public class FuelInfoController {
    private final FuelInfoService fuelInfoService;

    @Autowired
    public FuelInfoController(FuelInfoService fuelInfoService) {
        this.fuelInfoService = fuelInfoService;
    }

    @PostMapping("/update")
    public void updateData() {
        fuelInfoService.updateFuelData();
    }
}
