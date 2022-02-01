package com.fueladvisor.fuelpriceparserservice.repository;

import com.fueladvisor.fuelpriceparserservice.model.FuelInfo;
import com.fueladvisor.fuelpriceparserservice.model.FuelType;
import com.fueladvisor.fuelpriceparserservice.model.GasStation;
import com.fueladvisor.fuelpriceparserservice.model.Region;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fueladvisor.fuelpriceparserservice.model.FuelType.*;

@Component
public class FuelDataParserImpl implements FuelDataParser {
    private static final String URL = "https://index.minfin.com.ua/markets/fuel/detail/";
    private static final List<FuelType> fuelTypes =
            List.of(A95_PLUS, A95, A92, DIESEL_FUEL, GAS);

    @Override
    public List<FuelInfo> parseFuelData() throws IOException {
        var document = fetchParsedPage();
        var dataElements = getTDHtmlDataElements(document);
        var fuelInfoList = new ArrayList<FuelInfo>();

        parseFuelDataHelper(dataElements, fuelInfoList);

        return fuelInfoList;
    }

    private void parseFuelDataHelper(List<Element> dataElements, List<FuelInfo> fuelInfoList) {
        String region = "";
        for (Element element : dataElements) {

            // means that should parse a region name
            if (element.childrenSize() == 1)
                region = parseRegion(element);
            else {
                var gasStationAndPrices = parseGasStationAndPrices(
                        element.getElementsByTag("td"));

                createFuelInfosForGasStation(region, gasStationAndPrices.getFirst(),
                        gasStationAndPrices.getSecond(), fuelInfoList);
            }
        }
    }

    private void createFuelInfosForGasStation(String region, String gasStation,
                                              List<Double> prices, List<FuelInfo> fuelInfoList) {
        // prices.size() == fuelTypes.size()
        for (int i = 0; i < prices.size(); i++) {
            FuelInfo fuelInfo = FuelInfo.builder()
                    .region(new Region(region))
                    .gasStation(new GasStation(gasStation))
                    .fuelType(fuelTypes.get(i))
                    .price(prices.get(i))
                    .build();

            fuelInfoList.add(fuelInfo);
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
