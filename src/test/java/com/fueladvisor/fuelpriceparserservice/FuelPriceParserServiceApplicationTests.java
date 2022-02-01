package com.fueladvisor.fuelpriceparserservice;

import com.fueladvisor.fuelpriceparserservice.model.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.repository.FuelDataParserImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class FuelPriceParserServiceApplicationTests {

    @Autowired
    FuelDataParserImpl fuelDataParser;

    @Test
    void testFuelDataParser() throws IOException {
        List<FuelInfo> fuelInfos = fuelDataParser.parseFuelData();
    }
}
