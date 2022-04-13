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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

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
        return mapFuelInfoListToDtoList(fuelInfos);
    }

    @Transactional
    public List<FuelInfoDto> getFuelInfosInAllRegionsByGasStation(String gasStation) throws IOException {
        List<FuelInfo> fuelInfos = fuelInfoRepository.getFuelInfosByGasStationName(gasStation);
        return mapFuelInfoListToDtoList(fuelInfos);
    }

    @Transactional
    public List<FuelInfoDto> getFuelInfosByRegionLatinNameAndGasStationName(String regionLatin, String gasStation) throws IOException {
        List<FuelInfo> fuelInfos = fuelInfoRepository.getFuelInfosByRegionLatinNameAndGasStationName(regionLatin, gasStation);
        return mapFuelInfoListToDtoList(fuelInfos);
    }

    private String getGasStationLogoName(String gasStation) {
        String gasStationLogoName = "no_image.jpg";
        switch (gasStation) {
            case "Олас":
                gasStationLogoName = "olas.jpg";
                break;
            case "БРСМ-Нафта":
                gasStationLogoName = "brsm.jpg";
                break;
            case "Mango":
                gasStationLogoName = "mango.jpg";
                break;
            case "Укрнафта":
                gasStationLogoName = "ukr_nafta.jpg";
                break;
            case "UPG":
                gasStationLogoName = "upg.jpg";
                break;
            case "Кворум":
                gasStationLogoName = "kvorum.jpg";
                break;
            case "SOCAR":
                gasStationLogoName = "socar.jpg";
                break;
            case "WOG":
                gasStationLogoName = "wog.jpg";
                break;
            case "ОККО":
                gasStationLogoName = "okko.jpg";
                break;
            case "Рур груп":
                gasStationLogoName = "rur.jpg";
                break;
            case "Маркет":
                gasStationLogoName = "market.jpg";
                break;
            case "Shell":
                gasStationLogoName = "shell.jpg";
                break;
            case "Нефтек":
                gasStationLogoName = "neftek.jpg";
                break;
            case "Укргаздобыча":
                gasStationLogoName = "ukr_gaz.jpg";
                break;
            case "Фактор":
                gasStationLogoName = "factor.jpg";
                break;
            case "Катрал":
                gasStationLogoName = "katal.jpg";
                break;
            case "Автотранс":
                gasStationLogoName = "auto_trans.jpg";
                break;
            case "Авиас":
                gasStationLogoName = "avias.jpg";
                break;
        }
        return gasStationLogoName;
    }

    private byte[] readLogoImage(String gasStationLogoName) throws IOException {
        Resource resource = new ClassPathResource("logo/" + gasStationLogoName);
        return FileCopyUtils.copyToByteArray(resource.getInputStream());
    }

    private byte[] getBase64LogoOfGasStation(String gasStation) throws IOException {
        String gasStationLogoName = getGasStationLogoName(gasStation);
        byte[] img = readLogoImage(gasStationLogoName);

        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(img);
    }

    private List<FuelInfoDto> mapFuelInfoListToDtoList(List<FuelInfo> fuelInfos) throws IOException {
        List<FuelInfoDto> fuelInfoDtoList = new ArrayList<>();

        for (FuelInfo fuelInfo : fuelInfos) {
            FuelInfoDto dto = mapToFuelInfoDto(fuelInfo);
            fuelInfoDtoList.add(dto);
        }

        return fuelInfoDtoList;
    }

    private FuelInfoDto mapToFuelInfoDto(FuelInfo fuelInfo) throws IOException {
        return FuelInfoDto.builder()
                .id(fuelInfo.getId())
                .fuelType(fuelInfo.getFuelType().getName())
                .region(fuelInfo.getRegion().getName())
                .gasStation(fuelInfo.getGasStation().getName())
                .price(fuelInfo.getPrice())
                .logo(getBase64LogoOfGasStation(fuelInfo.getGasStation().getName()))
                .build();
    }
}
