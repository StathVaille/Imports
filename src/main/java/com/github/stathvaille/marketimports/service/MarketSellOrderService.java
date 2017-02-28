package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Returns lists of items currently on the market in sell orders
 */
@Service
public class MarketSellOrderService {

    private static final String urlTemplate = "https://esi.tech.ccp.is/latest/markets/%s/orders/?datasource=tranquility&order_type=sell&page=1&type_id=%s";
    private static final Logger logger = LoggerFactory.getLogger(MarketSellOrderService.class);

    public List<MarketOrder> getItemOrders(ImportLocation importLocation, Item item){
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(urlTemplate, importLocation.getRegionId(), item.getTypeId());
        MarketOrder[] marketOrderArray = restTemplate.getForObject(url, MarketOrder[].class);

        List<MarketOrder> marketOrders = Arrays.asList(marketOrderArray);
        logger.info(String.format("Retrieved %d market orders for %s in region %s", marketOrders.size(),
                                                                                            item.getName().getEn(),
                                                                                            importLocation.getRegionName()));

        marketOrders = filterToStation(marketOrders, importLocation.getStationId());
        logger.info(String.format("Filtered market orders down to %d in station %s", marketOrders.size(), importLocation.getStationId()));

        return marketOrders;
    }

    public Map<Long, List<MarketOrder>> getMultipleItemOrders(ImportLocation importLocation, List<Item> items){
        logger.info(String.format("Getting market orders at %s for %d distinct types", importLocation.getStationName(), items.size()));
        Map<Long, List<MarketOrder>> marketOrders = items.parallelStream()
                .map(item -> getItemOrders(importLocation, item))
                .flatMap(marketOrdersList -> marketOrdersList.stream())
                .collect(Collectors.groupingBy(MarketOrder::getType_id));
        logger.info("Got " + marketOrders.size() + " market orders");
        return marketOrders;
    }

    private List<MarketOrder> filterToStation(List<MarketOrder> marketOrders, long stationId) {
    return marketOrders.stream()
            .filter(marketOrder -> marketOrder.getLocation_id() == stationId)
            .collect(Collectors.toList());
    }
}
