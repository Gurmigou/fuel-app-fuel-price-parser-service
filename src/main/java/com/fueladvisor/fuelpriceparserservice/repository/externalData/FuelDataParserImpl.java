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

import static com.fueladvisor.fuelpriceparserservice.model.entity.FuelType.*;

@Component
public class FuelDataParserImpl implements FuelDataParser {
    private static final String URL = "https://index.minfin.com.ua/markets/fuel/detail/";
    private static final List<FuelType> fuelTypes =
            List.of(A95_PLUS, A95, A92, DIESEL_FUEL, GAS);

    @Override
    public FuelDataParsedResult parseFuelData() throws IOException {
        var document = fetchParsedPage();
        var dataElements = getTDHtmlDataElements(document);

        return parseFuelDataHelper(dataElements);
    }

    private FuelDataParsedResult parseFuelDataHelper(List<Element> dataElements) {
        String regionName = "";
        var regionsMap = new HashMap<String, Region>();
        var gasStationsMap = new HashMap<String, GasStation>();
        var fuelInfoList = new ArrayList<FuelInfo>();

        for (Element element : dataElements) {
            // means that should parse a region name
            if (element.childrenSize() == 1)
                regionName = parseRegion(element);
            else {
                var gasStationAndPrices = parseGasStationAndPrices(
                        element.getElementsByTag("td"));

                // retrieve region or create a new one if not exists
                Region region = regionsMap.computeIfAbsent(regionName, Region::new);

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
