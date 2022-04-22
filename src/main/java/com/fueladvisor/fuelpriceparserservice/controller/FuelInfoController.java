package com.fueladvisor.fuelpriceparserservice.controller;

import com.fueladvisor.fuelpriceparserservice.model.dto.GasStationDetailsDto;
import com.fueladvisor.fuelpriceparserservice.model.dto.GasStationLogoDto;
import com.fueladvisor.fuelpriceparserservice.model.entity.GasStationDetails;
import com.fueladvisor.fuelpriceparserservice.service.FuelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
                                         @RequestParam(required = false) String gasStationId) {
        if (regionLatin == null && gasStationId == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Parameters regionLatin and gasStation cannot be both null");
        }
        try {
            Object response;
            if (regionLatin != null && gasStationId != null)
                response = fuelInfoService.getFuelInfosByRegionLatinNameAndGasStationId(regionLatin, gasStationId);
            else if (regionLatin != null)
                response = fuelInfoService.getFuelInfosInRegion(regionLatin);
            else
                response = fuelInfoService.getFuelInfosInAllRegionsByGasStationId(gasStationId);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/logo")
    public ResponseEntity<?> getGasStationLogo(@RequestParam String gasStationId) {
        try {
            GasStationLogoDto gasStationLogo = fuelInfoService.getGasStationLogoById(gasStationId);
            return ResponseEntity.ok(gasStationLogo);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getGasStationDetails(@RequestParam String gasStationId) {
        try {
            GasStationDetailsDto gasStationDetails = fuelInfoService.getGasStationDetails(gasStationId);
            return ResponseEntity.ok(gasStationDetails);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping("/details")
    public ResponseEntity<?> updateGasStationDetails(@RequestBody GasStationDetailsDto gasStationDetailsDto) {
        try {
            GasStationDetails gasStationDetails = fuelInfoService.updateGasStationDetails(gasStationDetailsDto);
            return ResponseEntity.ok(gasStationDetails);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}