package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Generates the cost for a 5% market buy out of an item.
 *
 * Exmaple URL: http://api.eve-central.com/api/marketstat/json?typeid=28211&usesystem=30002187
 * e.g. https://esi.tech.ccp.is/latest/markets/10000014/orders/?datasource=tranquility&order_type=sell&page=1&type_id=2185
 */
@Service
public class MarketBuyPriceService {

    private final static String apiTemplate = "http://api.eve-central.com/api/marketstat/json?typeid=%s&usesystem=30002187";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Double getItem5PercentBuyPrice(Item item, ImportLocation location){
        logger.info("Calculating a 5% buy price for " + item.getName() + " in " + location.getSystemName());

        try{


            RestTemplate restTemplate = new RestTemplate();
            String url = String.format(apiTemplate, item.getTypeId(), location.getSystemId());
            Map[] itemBuyPrices = restTemplate.getForObject(url, HashMap[].class);
            Map itemBuyPricesMap = itemBuyPrices [0];
            Map itemSellPricesMap = (Map) itemBuyPricesMap.get("sell");
            Double itemFivePercentPrice = (Double) itemSellPricesMap.get("fivePercent");
            return itemFivePercentPrice;
        }
        catch (HttpClientErrorException e){
            logger.error("Error calling API getting sell orders for " + item + ". No data will be returned", e.getMessage());
            return 0.0;
        }
    }

    public Map<Item, Double> getItem5PercentBuyPrice(List<Item> items, ImportLocation location) {
        Map<Item, Double> itemBuyPrices = new HashMap<>();

        try {
            ForkJoinPool forkJoinPool = new ForkJoinPool(30);
            forkJoinPool.submit(() ->
                    items.parallelStream().forEach(item ->
                            itemBuyPrices.put(item, getItem5PercentBuyPrice(item, location))
                    )
            ).get();
        }
        catch (ExecutionException | InterruptedException e ){
            throw new RuntimeException("Error getting market buy price", e);
        }


        return itemBuyPrices;
    }
}
