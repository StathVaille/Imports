package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates the cost for a 5% market buy out of an item
 */
@Service
public class MarketBuyPriceService {

    private final static String apiTemplate = "http://api.eve-central.com/api/marketstat/json?typeid=28211&usesystem=30002187";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Long getItem5PercentBuyPrice(Item item){
        return 0L;
    }

    public Map<Item, Long> getItem5PercentBuyPrice(List<Item> items){
        return new HashMap<>();
    }
}
