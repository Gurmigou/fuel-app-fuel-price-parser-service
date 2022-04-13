package com.fueladvisor.fuelpriceparserservice.controller;

import com.fueladvisor.fuelpriceparserservice.model.dto.FuelInfoDto;
import com.fueladvisor.fuelpriceparserservice.service.FuelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fuel-info")
public class FuelInfoController {
    private final FuelInfoService fuelInfoService;

    @Autowired
    public FuelInfoController(FuelInfoService fuelInfoService) {
        this.fuelInfoService = fuelInfoService;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateData() {
        try {
            Optional<Integer> result = fuelInfoService.updateFuelData();
            return result
                    .map(integer -> ResponseEntity.ok(String.format("%d fuel objects were parsed", integer)))
                    .orElseGet(() -> ResponseEntity.ok("Nothing was parsed"));
        } catch (IOException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.toString());
        }
    }

    @GetMapping
    public ResponseEntity<?> getFuelInfo(@RequestParam(required = false) String regionLatin,
                                         @RequestParam(required = false) String gasStation) {
        System.out.println(1);
        if (regionLatin == null && gasStation == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Parameters regionLatin and gasStation cannot be both null");
        }
        System.out.println(2);
        try {
            List<FuelInfoDto> fuelInfoDtos;
            System.out.println(3);
            if (regionLatin != null && gasStation != null) {
                System.out.println(4);
                fuelInfoDtos = fuelInfoService.getFuelInfosByRegionLatinNameAndGasStationName(regionLatin, gasStation);
            }
            else if (regionLatin != null) {
                System.out.println(5);
                fuelInfoDtos = fuelInfoService.getFuelInfosInRegion(regionLatin);
            }
            else {
                System.out.println(6);
                fuelInfoDtos = fuelInfoService.getFuelInfosInAllRegionsByGasStation(gasStation);
            }

            System.out.println(7 + ": " + fuelInfoDtos);
            return ResponseEntity.ok(fuelInfoDtos);
        } catch (IOException e) {
            System.out.println(8);
            return ResponseEntity.badRequest().body(e);
        }
    }
}