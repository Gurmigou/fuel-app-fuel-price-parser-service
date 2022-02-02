package com.fueladvisor.fuelpriceparserservice;

import com.fueladvisor.fuelpriceparserservice.repository.externalData.FuelDataParserImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class FuelPriceParserServiceApplicationTests {

    @Autowired
    FuelDataParserImpl fuelDataParser;

    @Test
    void testFuelDataParser() throws IOException {

    }
}
