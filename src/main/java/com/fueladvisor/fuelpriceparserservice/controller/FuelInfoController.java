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
    public ResponseEntity<List<FuelInfoDto>> getFuelInfoInRegion(@RequestParam String regionLatin) {
        List<FuelInfoDto> fuelInfosDto = fuelInfoService.getFuelInfosInRegion(regionLatin);
        return ResponseEntity.ok(fuelInfosDto);
    }
}
