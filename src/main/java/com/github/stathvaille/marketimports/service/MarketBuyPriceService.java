package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates the cost for a 5% market buy out of an item.
 *
 * Exmaple URL: http://api.eve-central.com/api/marketstat/json?typeid=28211&usesystem=30002187
 */
@Service
public class MarketBuyPriceService {

    private final static String apiTemplate = "http://api.eve-central.com/api/marketstat/json?typeid=%s&usesystem=30002187";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Double getItem5PercentBuyPrice(Item item, ImportLocation location){
        logger.info("Calculating a 5% buy price for " + item.getName() + " in " + location.getSystemName());

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(apiTemplate, item.getTypeId(), location.getSystemId());
        Map[] itemBuyPrices = restTemplate.getForObject(url, HashMap[].class);
        Map itemBuyPricesMap = itemBuyPrices [0];
        Map itemSellPricesMap = (Map) itemBuyPricesMap.get("sell");
        Double itemFivePercentPrice = (Double) itemSellPricesMap.get("fivePercent");
        return itemFivePercentPrice;
    }

    public Map<Item, Double> getItem5PercentBuyPrice(List<Item> items, ImportLocation location){
        Map<Item, Double> itemBuyPrices = new HashMap<>();
        items.parallelStream().forEach(item ->
            itemBuyPrices.put(item, getItem5PercentBuyPrice(item, location))
        );
        return itemBuyPrices;
    }
}
