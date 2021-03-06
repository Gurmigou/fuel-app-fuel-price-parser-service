package com.fueladvisor.fuelpriceparserservice.service;

import com.fueladvisor.fuelpriceparserservice.model.entity.FuelType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fueladvisor.fuelpriceparserservice.model.entity.FuelType.*;

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
        put("Автотранс", "autotrans");
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

    public static Map<String, List<String>> details = new HashMap<String, List<String>>() {{
        put("autotrans", Arrays.asList("+38 050-305-43-08", "office@autotrans.ua"));
        put("avias", Arrays.asList("0-800-501-788", "null"));
        put("okko", Arrays.asList("0 800 501 101", "null"));
        put("brsm", Arrays.asList("0 800 303 404", "info@brsm-nafta.com.ua"));
        put("factor", Arrays.asList("", ""));
        put("katral", Arrays.asList("+380 482 397 046", ""));
        put("mango", Arrays.asList("+38 044 528 0111", "info@ultraoil.com.ua"));
        put("neftek", Arrays.asList("0-800-75-70-05", "hotline@neftek.ua"));
        put("olas", Arrays.asList("+38 (0362) 68-33-50", ""));
        put("rur", Arrays.asList("", ""));
        put("shell", Arrays.asList("0 800 500 423", "info.shell@acc.com.ua"));
        put("socar", Arrays.asList("0 (800) 50 85 85", "info@socar.ua"));
        put("ukr_nafta", Arrays.asList("0 800 404 000", "hotline@ukrnafta.com"));
        put("upg", Arrays.asList("0 800 500 064", ""));
        put("urk_gaz", Arrays.asList("", ""));
        put("urk_petrol", Arrays.asList("(03342) 2-08-76", "ukrpetrol@gmail.com"));
        put("market", Arrays.asList("", "info@azs-market.com.ua"));
        put("wog", Arrays.asList("0 800 300 525", ""));
    }};

    public static List<FuelType> allFuelTypes = Arrays.asList(A92, A95, A95_PLUS, DIESEL_FUEL, DIESEL_FUEL_PLUS, GAS);

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
