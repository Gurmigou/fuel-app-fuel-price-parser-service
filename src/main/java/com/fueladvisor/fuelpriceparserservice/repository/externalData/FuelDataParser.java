package com.fueladvisor.fuelpriceparserservice.repository.externalData;

import com.fueladvisor.fuelpriceparserservice.model.FuelDataParsedResult;

import java.io.IOException;

public interface FuelDataParser {
    FuelDataParsedResult parseFuelData() throws IOException;
}
