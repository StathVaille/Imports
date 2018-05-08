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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Returns lists of items currently on the market in sell orders
 * e.g. https://esi.tech.ccp.is/latest/markets/10000014/orders/?datasource=tranquility&order_type=sell&page=1&type_id=2185
 */
@Service
public class MarketSellOrderService {

    private static final String urlTemplate = "https://esi.tech.ccp.is/latest/markets/%s/orders/?datasource=tranquility&order_type=sell&page=1&type_id=%s";
    private static final Logger logger = LoggerFactory.getLogger(MarketSellOrderService.class);

    private final ItemService itemService;

    public MarketSellOrderService(ItemService itemService) {
        this.itemService = itemService;
    }


    public List<MarketOrder> getItemOrders(ImportLocation importLocation, Item item){
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(urlTemplate, importLocation.getRegionId(), item.getTypeId());
        MarketOrder[] marketOrderArray = restTemplate.getForObject(url, MarketOrder[].class);

        List<MarketOrder> marketOrders = Arrays.asList(marketOrderArray);
        logger.info(String.format("Retrieved %d market orders for '%s' in region %s", marketOrders.size(),
                                                                                    item.getName().getEn(),
                                                                                    importLocation.getRegionName()));

        marketOrders = filterToStation(marketOrders, importLocation.getStationId());
        logger.info(String.format("Filtered market orders down to %d in station %s", marketOrders.size(), importLocation.getStationName()));

        return marketOrders;
    }

    /**
     * Get market information of sell orders at a specific location
     * @param importLocation The market to search sell orders for
     * @param items A list of items to search sell orders for
     */
    public Map<Item, List<MarketOrder>> getMultipleItemOrders(ImportLocation importLocation, List<Item> items){
        logger.info(String.format("Getting market orders at %s for %d distinct types", importLocation.getStationName(), items.size()));

        try {
            ForkJoinPool forkJoinPool = new ForkJoinPool(30);
            Map<Item, List<MarketOrder>> marketOrders = forkJoinPool.submit(() ->
                    items.parallelStream()
                        .map(item -> getItemOrders(importLocation, item))
                        .flatMap(marketOrdersList -> marketOrdersList.stream())
                        .collect(Collectors.groupingBy(marketOrder -> itemService.getItemById(marketOrder.getType_id()).get()))
            ).get();
            logger.info("Got " + marketOrders.size() + " market orders");
            return marketOrders;
        }
        catch (ExecutionException | InterruptedException e ){
            throw new RuntimeException("Error getting market buy price", e);
        }


    }

    private List<MarketOrder> filterToStation(List<MarketOrder> marketOrders, long stationId) {
    return marketOrders.stream()
            .filter(marketOrder -> marketOrder.getLocation_id() == stationId)
            .collect(Collectors.toList());
    }
}
