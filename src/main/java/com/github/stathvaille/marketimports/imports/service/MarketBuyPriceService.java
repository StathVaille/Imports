package com.github.stathvaille.marketimports.imports.service;

import com.github.stathvaille.marketimports.imports.controller.BaseESIController;
import com.github.stathvaille.marketimports.esi.domain.MarketOrder;
import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.esi.service.MultiPageESIRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Calculate the minimum sell price for an item at a location
 *
 * Example URL: https://esi.evetech.net/latest/markets/10000043/orders/?datasource=tranquility&order_type=sell&page=2
 */
@Service
public class MarketBuyPriceService extends BaseESIController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MultiPageESIRequest<MarketOrder> multiPageESIRequest;

    public MarketBuyPriceService(){
        multiPageESIRequest = new MultiPageESIRequest<>();
    }

    public Map<Item, Optional<MarketOrder>> getMinSalesPrices(List<Item> items, ImportLocation location, OAuth2AuthenticationToken authentication) {
        List<MarketOrder> stationMarketOrders = getAllSellOrdersAtStation(location, authentication);

        Map<Item, Optional<MarketOrder>> itemBuyPrices = new HashMap<>();
        items.stream().forEach(item -> itemBuyPrices.put(item, getMinSalesPrices(item, location, stationMarketOrders)));
        return itemBuyPrices;
    }

    private Optional<MarketOrder> getMinSalesPrices(Item item, ImportLocation location, List<MarketOrder> stationMarketOrders) {
        Optional<MarketOrder> cheapestMarketOrder =  stationMarketOrders.stream()
                                                                        .filter(marketOrder -> marketOrder.getType_id() == item.getTypeId())
                                                                        .min(Comparator.comparing(MarketOrder::getPrice));

        if (!cheapestMarketOrder.isPresent()) {
            logger.warn("Could not find any sell orders for item '" + item.getName() + "' in location " + location);
        }
        return cheapestMarketOrder;
    }

    private List<MarketOrder> getAllSellOrdersAtStation(ImportLocation location, OAuth2AuthenticationToken authentication){
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("region_id", Long.toString(location.getRegionId()));
        UriComponents uriComponents =
                UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("esi.evetech.net")
                        .path("/latest/markets/{region_id}/orders/")
                        .queryParam("datasource", "tranquility")
                        .queryParam("order_type", "sell")
                        .build()
                        .expand(templateParams)
                        .encode();


        ParameterizedTypeReference esiObjectType = new ParameterizedTypeReference<List<MarketOrder>>() {};
        RestTemplate restTemplate = getOAuthRestTemplate(authentication);

        URI uri = uriComponents.toUri();
        logger.info("Retrieving all sell orders in region: " + location.getRegionName() + ": " + uri);
        List<MarketOrder> marketOrders = multiPageESIRequest.makeESICall(uri, esiObjectType, restTemplate);
        logger.info("Found " + marketOrders.size() + " market orders in " + location.getRegionName());

        List<MarketOrder> stationMarketOrders = marketOrders.stream()
                                                   .filter(marketOrder -> marketOrder.getLocation_id() == location.getStationId())
                                                   .filter(marketOrder -> !marketOrder.is_buy_order())
                                                   .collect(Collectors.toList());

        logger.info("Filtered down to " + stationMarketOrders.size() + " sell orders in station " + location.getStationName());
        return stationMarketOrders;
    }
}
