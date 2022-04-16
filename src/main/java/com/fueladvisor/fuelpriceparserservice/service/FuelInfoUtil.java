package com.fueladvisor.fuelpriceparserservice.service;

import com.fueladvisor.fuelpriceparserservice.model.entity.FuelType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fueladvisor.fuelpriceparserservice.model.entity.FuelType.*;
import static com.fueladvisor.fuelpriceparserservice.model.entity.FuelType.GAS;

public class FuelInfoUtil {
    public static final Map<String, String> gasStationMapping = new HashMap<String, String>() {{
        put("Олас", "olas");
        put("БРСМ-Нафта", "brsm");
        put("Mango ", "mango");
        put("Укрнафта", "ukr_nafta");
        put("UPG", "upg");
        put("SOCAR", "socar");
        put("WOG", "wog");
        put("ОККО", "okko");
        put("Рур груп", "rur");
        put("Маркет", "market");
        put("Shell", "shell");
        put("Нефтек", "neftek");
        put("Укр-Петроль", "urk_petrol");
        put("Укргаздобыча", "urk_gaz");
        put("Фактор", "factor");
        put("Катрал", "katral");
        put("Автотранс", "auto_trans");
        put("Авиас", "avias");
        put("Кворум", "kvorum");
        put("Mango", "mango");
    }};

    public static final Map<String, String> regions = new HashMap<String, String>(){{
        put("Черкасская обл.", "Cherkas'ka oblast");
        put("Черниговская обл.", "Chernihivs'ka oblast");
        put("Черновицкая обл.", "Chernivets'ka oblast");
        put("Днепропетровская обл.", "Dnipropetrovs'ka oblast");
        put("Донецкая обл.", "Donets'ka oblast");
        put("Ивано-Франковская обл.", "Ivano-Frankivs'ka oblast");
        put("Харьковская обл.", "Kharkivs'ka oblast");
        put("Херсонская обл.", "Khersons'ka oblast");
        put("Хмельницкая обл.", "Khmel'nyts'ka oblast");
        put("Кировоградская обл.", "Kirovohrads'ka oblast");
        put("Киевская обл.", "Kyivs'ka oblast");
        put("Львовская обл.", "L'vivs'ka oblast");
        put("Луганская обл.", "Luhans'ka oblast");
        put("Николаевская обл.", "Mykolaivs'ka oblast");
        put("Одесская обл.", "Odes'ka oblast");
        put("Полтавская обл.", "Poltavs'ka oblast");
        put("Ровенская обл.", "Rivnens'ka oblast");
        put("Сумская обл.", "Sums'ka oblast");
        put("Тернопольская обл.", "Ternopil's'ka oblast");
        put("Винницкая обл.", "Vinnyts'ka oblast");
        put("Волынская обл.", "Volyns'ka oblast");
        put("Запорожская обл.", "Zaporiz'ka oblast");
        put("Житомирская обл.", "Zhytomyrs'ka oblast");
        put("Закарпатская обл.", "Zakarpats'ka oblast'");
    }};

    public static boolean isGasStationExists(String gasStationId) {
        return gasStationMapping.containsValue(gasStationId);
    }

    public static FuelType mapFuelType(String fuelTypeName) {
        if (fuelTypeName.contains("92"))
            return A92;
        if (fuelTypeName.contains("95+"))
            return A95;
        if (fuelTypeName.contains("95"))
            return A95_PLUS;
        if (fuelTypeName.contains("ДТ"))
            return DIESEL_FUEL;
        if (fuelTypeName.contains("ДТ+"))
            return DIESEL_FUEL_PLUS;
        return GAS;
    }
}
