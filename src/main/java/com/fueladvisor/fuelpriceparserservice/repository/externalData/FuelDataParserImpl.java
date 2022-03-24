package com.fueladvisor.fuelpriceparserservice.repository.externalData;

import com.fueladvisor.fuelpriceparserservice.model.FuelDataParsedResult;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelType;
import com.fueladvisor.fuelpriceparserservice.model.entity.GasStation;
import com.fueladvisor.fuelpriceparserservice.model.entity.Region;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fueladvisor.fuelpriceparserservice.model.entity.FuelType.*;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@Component
public class FuelDataParserImpl implements FuelDataParser {
    private static final String URL = "https://index.minfin.com.ua/markets/fuel/detail/";
    private static final List<FuelType> fuelTypes =
            List.of(A95_PLUS, A95, A92, DIESEL_FUEL, GAS);
    private static final Map<String, String> regions = ofEntries(
            entry("Черкасская обл.", "Cherkas'ka oblast"),
            entry("Черниговская обл.", "Chernihivs'ka oblast"),
            entry("Черновицкая обл.", "Chernivets'ka oblast"),
            entry("Днепропетровская обл.", "Dnipropetrovs'ka oblast"),
            entry("Донецкая обл.", "Donets'ka oblast"),
            entry("Ивано-Франковская обл.", "Ivano-Frankivs'ka oblast"),
            entry("Харьковская обл.", "Kharkivs'ka oblast"),
            entry("Херсонская обл.", "Khersons'ka oblast"),
            entry("Хмельницкая обл.", "Khmel'nyts'ka oblast"),
            entry("Кировоградская обл.", "Kirovohrads'ka oblast"),
            entry("Киевская обл.", "Kyivs'ka oblast"),
            entry("Львовская обл.", "L'vivs'ka oblast"),
            entry("Луганская обл.", "Luhans'ka oblast"),
            entry("Николаевская обл.", "Mykolaivs'ka oblast"),
            entry("Одесская обл.", "Odes'ka oblast"),
            entry("Полтавская обл.", "Poltavs'ka oblast"),
            entry("Ровенская обл.", "Rivnens'ka oblast"),
            entry("Сумская обл.", "Sums'ka oblast"),
            entry("Тернопольская обл.", "Ternopil's'ka oblast"),
            entry("Винницкая обл.", "Vinnyts'ka oblast"),
            entry("Волынская обл.", "Volyns'ka oblast"),
            entry("Запорожская обл.", "Zaporiz'ka oblast"),
            entry("Житомирская обл.", "Zhytomyrs'ka oblast"),
            entry("Закарпатская обл.", "Zakarpats'ka oblast'")
    );

    @Override
    public FuelDataParsedResult parseFuelData() throws IOException {
        Document document = fetchParsedPage();
        List<Element> dataElements = getTDHtmlDataElements(document);

        return parseFuelDataHelper(dataElements);
    }

    private FuelDataParsedResult parseFuelDataHelper(List<Element> dataElements) {
        String regionName = "";
        Map<String, Region> regionsMap = new HashMap<>();
        Map<String, GasStation> gasStationsMap = new HashMap<>();
        List<FuelInfo> fuelInfoList = new ArrayList<>();

        for (Element element : dataElements) {
            // means that should parse a region name
            if (element.childrenSize() == 1)
                regionName = parseRegion(element);
            else {
                Pair<String, List<Double>> gasStationAndPrices = parseGasStationAndPrices(
                        element.getElementsByTag("td"));

                // retrieve region or create a new one if not exists
                Region region = regionsMap.computeIfAbsent(regionName,
                        name -> new Region(name, regions.get(name)));

                // retrieve gasStation or create a new one if not exists
                GasStation gasStation = gasStationsMap.computeIfAbsent(
                        gasStationAndPrices.getFirst(), GasStation::new);

                createFuelInfosForGasStation(
                        region,
                        gasStation,
                        gasStationAndPrices.getSecond(),
                        fuelInfoList
                );
            }
        }

        return new FuelDataParsedResult(fuelInfoList, regionsMap.values(), gasStationsMap.values());
    }

    private void createFuelInfosForGasStation(Region region,
                                              GasStation gasStation,
                                              List<Double> prices,
                                              List<FuelInfo> fuelInfoResultList) {
        // prices.size() == fuelTypes.size()
        for (int i = 0; i < prices.size(); i++) {
            FuelInfo fuelInfo = FuelInfo.builder()
                    .region(region)
                    .gasStation(gasStation)
                    .fuelType(fuelTypes.get(i))
                    .price(prices.get(i))
                    .build();

            fuelInfoResultList.add(fuelInfo);
        }
    }

    private Document fetchParsedPage() throws IOException {
        return Jsoup.connect(URL).get();
    }

    private List<Element> getTDHtmlDataElements(Document document) {
        Elements allTd = document.getElementsByTag("tr");
        return allTd.subList(1, allTd.size() - 1);
    }

    private String parseRegion(Element regionElement) {
        return regionElement
                .child(0)
                .child(0)
                .html();
    }

    private Pair<String, List<Double>> parseGasStationAndPrices(Elements elements) {
        String gasStation = elements.get(0).text();
        List<Double> prices = new ArrayList<>();

        for (int i = 1; i < elements.size(); i++) {
            String price = elements.get(i).text();
            if (!price.isEmpty()) {
                price = price.replace(',', '.');
                prices.add(Double.parseDouble(price));
            }
        }

        return Pair.of(gasStation, prices);
    }
}
