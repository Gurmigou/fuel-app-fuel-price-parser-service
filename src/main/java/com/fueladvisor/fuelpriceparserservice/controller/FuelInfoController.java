package com.fueladvisor.fuelpriceparserservice.controller;

import com.fueladvisor.fuelpriceparserservice.model.dto.FuelInfoDto;
import com.fueladvisor.fuelpriceparserservice.service.FuelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fuel-info")
public class FuelInfoController {
    private final FuelInfoService fuelInfoService;

    @Autowired
    public FuelInfoController(FuelInfoService fuelInfoService) {
        this.fuelInfoService = fuelInfoService;
    }

//    @PostMapping("/update")
//    public void updateData() {
//        fuelInfoService.updateFuelData();
//    }

    @GetMapping
    public ResponseEntity<List<FuelInfoDto>> getFuelInfoInRegion(@RequestParam String region) {
        List<FuelInfoDto> fuelInfosDto = fuelInfoService.getFuelInfosInRegion(region);
        return ResponseEntity.ok(fuelInfosDto);
    }
}
