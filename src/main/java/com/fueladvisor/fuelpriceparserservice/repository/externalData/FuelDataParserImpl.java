package com.fueladvisor.fuelpriceparserservice.repository.externalData;

import com.fueladvisor.fuelpriceparserservice.model.FuelDataParsedResult;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.entity.FuelType;
import com.fueladvisor.fuelpriceparserservice.model.entity.GasStation;
import com.fueladvisor.fuelpriceparserservice.model.entity.Region;
import com.fueladvisor.fuelpriceparserservice.service.FuelInfoUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FuelDataParserImpl implements FuelDataParser {
    private static final String URL = "https://index.minfin.com.ua/markets/fuel/detail/";

    @Override
    public FuelDataParsedResult parseFuelData() throws IOException {
        Document document = fetchParsedPage();
        List<Element> dataElements = getTDHtmlDataElements(document);
        List<FuelType> availableFuelTypes = parseAvailableFuelTypes(dataElements.get(0));

        return parseFuelDataHelper(dataElements.subList(1, dataElements.size()), availableFuelTypes);
    }

    private FuelDataParsedResult parseFuelDataHelper(List<Element> dataElements, List<FuelType> availableFuelTypes) {
        String regionName = "";
        Map<String, Region> regionsMap = new HashMap<>();
        Map<String, GasStation> gasStationsMap = new HashMap<>();
        List<FuelInfo> fuelInfoList = new ArrayList<>();

        for (Element element : dataElements) {
            // means that should parse a region name
            if (element.childrenSize() == 1)
                regionName = parseRegion(element);
            else {
                String tmp = element.getElementsByTag("td").get(0).text();

                Pair<String, Pair<boolean[], List<Double>>> gasStationAndPrices =
                        parseGasStationAndPrices(element.getElementsByTag("td"), availableFuelTypes);

                // retrieve region or create a new one if not exists
                Region region = regionsMap.computeIfAbsent(regionName,
                        name -> new Region(name, FuelInfoUtil.regions.get(name)));

                // retrieve gasStation or create a new one if not exists
                GasStation gasStation = gasStationsMap.computeIfAbsent(
                        gasStationAndPrices.getFirst(), name -> {
                            String gasStationId = "undefined";
                            if (FuelInfoUtil.gasStationMapping.containsKey(name))
                                gasStationId = FuelInfoUtil.gasStationMapping.get(name);
                            return new GasStation(gasStationId, name);
                        });

                createFuelInfosForGasStation(
                        region,
                        gasStation,
                        gasStationAndPrices.getSecond().getSecond(),
                        gasStationAndPrices.getSecond().getFirst(),
                        fuelInfoList,
                        availableFuelTypes
                );
            }
        }

        return new FuelDataParsedResult(fuelInfoList, regionsMap.values(), gasStationsMap.values());
    }

    private void createFuelInfosForGasStation(Region region, GasStation gasStation, List<Double> prices,
                                              boolean[] markArray, List<FuelInfo> fuelInfoResultList,
                                              List<FuelType> availableFuelTypes) {
        // markArray.length == prices.size()
        for (int i = 0; i < prices.size(); i++) {
            // if true - fuel type is available for current gas station in the specified region
            if (markArray[i]) {
                FuelInfo fuelInfo = FuelInfo.builder()
                        .region(region)
                        .gasStation(gasStation)
                        .fuelType(availableFuelTypes.get(i))
                        .price(prices.get(i))
                        .build();

                System.out.println(fuelInfo + " Marks: " + Arrays.toString(markArray) + " Prices: " + prices);

                fuelInfoResultList.add(fuelInfo);
            }
        }
    }

    private Document fetchParsedPage() throws IOException {
        return Jsoup.connect(URL).get();
    }

    private List<Element> getTDHtmlDataElements(Document document) {
        Elements allTd = document.getElementsByTag("tr");
        return allTd.subList(0, allTd.size() - 1);
    }

    private String parseRegion(Element regionElement) {
        return regionElement
                .child(0)
                .child(0)
                .html();
    }

    private Pair<String, Pair<boolean[], List<Double>>> parseGasStationAndPrices(
            Elements elements, List<FuelType> availableFuelTypes) {

        String gasStation = elements.get(0).text();
        List<Double> prices = new ArrayList<>();

        boolean[] markArray = new boolean[availableFuelTypes.size()];

        for (int i = 2; i < elements.size(); i++) {
            String price = elements.get(i).text();
            if (!price.isEmpty()) {
                price = price.replace(',', '.');
                prices.add(Double.parseDouble(price));
                markArray[i - 2] = true;
            } else
                prices.add(null);
        }

        return Pair.of(gasStation, Pair.of(markArray , prices));
    }

    private List<FuelType> parseAvailableFuelTypes(Element element) {
        return element.children()
                .stream()
                .skip(1)
                .map(Element::text)
                .map(FuelInfoUtil::mapFuelType)
                .collect(Collectors.toList());
    }
}