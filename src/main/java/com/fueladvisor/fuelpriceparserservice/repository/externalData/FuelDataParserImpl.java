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
import java.util.*;

import static com.fueladvisor.fuelpriceparserservice.model.entity.FuelType.*;

@Component
public class FuelDataParserImpl implements FuelDataParser {
    private static final String URL = "https://index.minfin.com.ua/markets/fuel/detail/";
    private static final List<FuelType> fuelTypes = Arrays.asList(A95_PLUS, A95, A92, DIESEL_FUEL, GAS);

    private static final Map<String, String> regions = new HashMap<String, String>(){{
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
