package com.fueladvisor.fuelpriceparserservice.repository;

import com.fueladvisor.fuelpriceparserservice.model.FuelInfo;

import java.io.IOException;
import java.util.List;

public interface FuelDataParser {
    List<FuelInfo> parseFuelData() throws IOException;

}
