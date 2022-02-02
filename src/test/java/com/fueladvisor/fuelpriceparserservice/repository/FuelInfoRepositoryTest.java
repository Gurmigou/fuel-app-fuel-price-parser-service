package com.fueladvisor.fuelpriceparserservice.repository;

import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FuelInfoRepositoryTest {

    @Autowired
    FuelInfoRepository fuelInfoRepository;

    @Test
    void getFuelInfoByRegionName() {
//        long b1 = System.nanoTime();
//        Optional<List<FuelInfo>> fuelInfosByRegionName2 = fuelInfoRepository.getFuelInfosByRegionName2("Черниговская обл.");
//
//        long a1 = System.nanoTime();
//        System.out.println((a1 - b1) / 1_000_000D + " millis");

//        assertThat(fuelInfo).isNotNull();
    }
}