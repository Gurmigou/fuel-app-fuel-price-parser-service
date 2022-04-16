package com.fueladvisor.fuelpriceparserservice.service;

import com.fueladvisor.fuelpriceparserservice.model.FuelDataParsedResult;
import com.fueladvisor.fuelpriceparserservice.model.dto.FuelInfoDto;
import com.fueladvisor.fuelpriceparserservice.model.dto.FuelPriceDto;
import com.fueladvisor.fuelpriceparserservice.model.dto.GasStationLogoDto;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.entity.GasStation;
import com.fueladvisor.fuelpriceparserservice.model.entity.Region;
import com.fueladvisor.fuelpriceparserservice.repository.FuelInfoRepository;
import com.fueladvisor.fuelpriceparserservice.repository.GasStationRepository;
import com.fueladvisor.fuelpriceparserservice.repository.RegionRepository;
import com.fueladvisor.fuelpriceparserservice.repository.externalData.FuelDataParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        FuelDataParsedResult parsedResult = fuelDataParser.parseFuelData();

        Iterable<Region> regions = regionRepository.findAll();

        if (!regions.iterator().hasNext()) {
            regionRepository.saveAll(parsedResult.getRegions());
        } else {
            Map<String, Region> regionsMap = new HashMap<>();
            regions.forEach(region -> regionsMap.put(region.getLatinName(), region));

            parsedResult.getRegions()
                    .forEach(parsedRegion -> {
                        Region foundRegion = regionsMap.get(parsedRegion.getLatinName());
                        parsedRegion.setId(foundRegion.getId());
                    });
        }

        Iterable<GasStation> gasStations = gasStationRepository.findAll();

        if (!gasStations.iterator().hasNext()) {
            gasStationRepository.saveAll(parsedResult.getGasStations());
        } else {
            Map<String, GasStation> gasStationsMap = new HashMap<>();
            gasStations.forEach(gasStation -> gasStationsMap.put(gasStation.getName(), gasStation));

            parsedResult.getGasStations()
                    .forEach(parsedGasStation -> {
                        GasStation foundGasStation = gasStationsMap.get(parsedGasStation.getName());
                        parsedGasStation.setId(foundGasStation.getId());
                    });
        }
        fuelInfoRepository.saveAll(parsedResult.getFuelInfoList());

        return Optional.of(parsedResult.getFuelInfoList().size());
    }

    @Transactional
    public List<FuelInfoDto> getFuelInfosInRegion(String regionLatin) throws IOException {
        if (Objects.equals(regionLatin, "Kyiv City"))
            regionLatin = "Kyivs'ka oblast";

        List<FuelInfo> fuelInfos = fuelInfoRepository.getFuelInfosByRegionLatinName(regionLatin);
        return aggregateAndMapFuelInfos(fuelInfos, (prev, cur) ->
                !Objects.equals(prev.getGasStation().getId(), cur.getGasStation().getId()));
    }

    @Transactional
    public List<FuelInfoDto> getFuelInfosInAllRegionsByGasStationId(String gasStationId) throws IOException {
        List<FuelInfo> fuelInfos = fuelInfoRepository.getFuelInfosByGasStationName(gasStationId);
        return aggregateAndMapFuelInfos(fuelInfos, (prev, cur) ->
                !Objects.equals(prev.getRegion().getId(), cur.getRegion().getId()));
    }

    @Transactional
    public FuelInfoDto getFuelInfosByRegionLatinNameAndGasStationId(
            String regionLatin, String gasStationId) throws IOException {

        List<FuelInfo> fuelInfos = fuelInfoRepository
                .getFuelInfosByRegionLatinNameAndGasStationName(regionLatin, gasStationId);

        List<FuelPriceDto> fuelPriceDtoList = fuelInfos
                .stream()
                .map(fuelInfo -> new FuelPriceDto(fuelInfo.getFuelType().getName(), fuelInfo.getPrice()))
                .collect(Collectors.toList());

        return fuelInfos.isEmpty() ? null : mapToFuelInfoDto(fuelInfos.get(0), fuelPriceDtoList);
    }

    public GasStationLogoDto getGasStationLogoById(String gasStationId) throws IOException {
        String imageName = getGasStationImageName(gasStationId);
        byte[] logo = readLogoImage(imageName);
        return new GasStationLogoDto(gasStationId, logo, FuelInfoUtil.isGasStationExists(gasStationId));
    }

    private String getGasStationImageName(String gasStationId) {
        return FuelInfoUtil.isGasStationExists(gasStationId)
                ? gasStationId + ".jpg"
                : "no_image.jpg";
    }

    private byte[] readLogoImage(String gasStationLogoName) throws IOException {
        Resource resource = new ClassPathResource("logo/" + gasStationLogoName);
        return FileCopyUtils.copyToByteArray(resource.getInputStream());
    }

    private List<FuelInfoDto> aggregateAndMapFuelInfos(List<FuelInfo> fuelInfos,
                                                       BiPredicate<FuelInfo, FuelInfo> biPredicate) {

        List<FuelInfoDto> fuelInfoDtoList = new ArrayList<>();

        FuelInfo prev = null;
        List<FuelPriceDto> fuelPriceDtoList = new ArrayList<>();
        for (FuelInfo fuelInfo : fuelInfos) {
            if (prev != null) {
                if (biPredicate.test(prev, fuelInfo)) {
                    fuelInfoDtoList.add(mapToFuelInfoDto(prev, fuelPriceDtoList));
                    fuelPriceDtoList = new ArrayList<>();
                }
            }
            fuelPriceDtoList.add(new FuelPriceDto(fuelInfo.getFuelType().getName(), fuelInfo.getPrice()));
            prev = fuelInfo;
        }

        if (prev != null) {
            fuelInfoDtoList.add(mapToFuelInfoDto(prev, fuelPriceDtoList));
        }

        return fuelInfoDtoList;
    }

    private FuelInfoDto mapToFuelInfoDto(FuelInfo fuelInfo, List<FuelPriceDto> fuelPriceDtoList) {
        return FuelInfoDto
                .builder()
                .gasStationId(fuelInfo.getGasStation().getId())
                .gasStationName(fuelInfo.getGasStation().getName())
                .region(fuelInfo.getRegion().getName())
                .fuelPrices(fuelPriceDtoList)
                .build();
    }
}
