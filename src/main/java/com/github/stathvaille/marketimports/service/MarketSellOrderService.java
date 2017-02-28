package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Returns lists of items currently on the market in sell ordered
 */
@Service
public class MarketSellOrderService {

    private static final String urlTemplate = "https://esi.tech.ccp.is/latest/markets/%s/orders/?datasource=tranquility&order_type=sell&page=1&type_id=%s";
    private static final Logger logger = LoggerFactory.getLogger(MarketSellOrderService.class);

    public List<MarketOrder> getItemOrders(long stationId, long typeId){
        // TODO look up region Id from station Id
        long regionId = 10000014;

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(urlTemplate, regionId, typeId);
        MarketOrder[] marketOrderArray = restTemplate.getForObject(url, MarketOrder[].class);

        List<MarketOrder> marketOrders = Arrays.asList(marketOrderArray);
        logger.info(String.format("Retrieved %d market orders for region %d", marketOrders.size(), regionId));

        marketOrders = filterToStation(marketOrders, stationId);
        logger.info(String.format("Filtered market orders down to %d in station %s", marketOrders.size(), stationId));

        return marketOrders;
    }

    private List<MarketOrder> filterToStation(List<MarketOrder> marketOrders, long stationId) {
        return marketOrders.stream()
                .filter(marketOrder -> marketOrder.getLocation_id() == stationId)
                .collect(Collectors.toList());
    }
}
