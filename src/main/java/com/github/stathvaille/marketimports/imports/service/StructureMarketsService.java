package com.github.stathvaille.marketimports.imports.service;

import com.github.stathvaille.marketimports.esi.ESIClient;
import com.github.stathvaille.marketimports.esi.domain.MarketOrder;
import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.items.staticdataexport.Item;
import com.github.stathvaille.marketimports.esi.MultiPageESIRequest;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class StructureMarketsService extends ESIClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MultiPageESIRequest<MarketOrder> multiPageESIRequest = new MultiPageESIRequest<>();


    public List<MarketOrder> getAllSellOrdersInStructure(ImportLocation location, OAuth2AuthenticationToken authentication) {
        logger.info("Getting all sell orders in structure: " + location.getStationName());

        RestTemplate resttemplate = getOAuthRestTemplate(authentication);

        // Query market structures
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("structure_id", Long.toString(location.getStationId()));
        UriComponents uriComponents =
                UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("esi.evetech.net")
                        .path("/latest/markets/structures/{structure_id}/")
                        .queryParam("datasource", "tranquility")
                        .build()
                        .expand(templateParams)
                        .encode();


        ParameterizedTypeReference esiObjectType = new ParameterizedTypeReference<List<MarketOrder>>() {};
        List<MarketOrder> marketOrders = multiPageESIRequest.makeESICall(uriComponents.toUri(), esiObjectType, resttemplate);
        // Remove buy orders
        marketOrders = marketOrders.stream()
                                   .filter(order -> !order.is_buy_order())
                                   .filter(order -> order.getRange().equals("region"))  // Buy orders are still coming up (i think a big). this is a bad filter that I think will help work around
                                   .collect(Collectors.toList());
        logger.info("Found " + marketOrders.size()  + " market orders in " + location.getStationName());
        return marketOrders;
    }

    @Async
    public CompletableFuture<StructureSellOrdersResults> getAllSellOrdersInStructure(ImportLocation location, List<Item> interestingItems, OAuth2AuthenticationToken authentication) {
        logger.info("Getting all sell orders on " + location.getStationName() + " for " + interestingItems.size() + " interesting items");
        List<MarketOrder> allSellOrders = getAllSellOrdersInStructure(location, authentication);

        Map<Item, List<MarketOrder>> itemOrders = new HashMap<>();

        for (MarketOrder order : allSellOrders) {
            Optional<Item> itemForOrder = interestingItems.stream().filter(item -> item.getTypeId() == order.getType_id()).findFirst();
            if (itemForOrder.isPresent()){

                // Add item to map if it does not exist
                if (!itemOrders.containsKey(itemForOrder.get())) {
                    itemOrders.put(itemForOrder.get(), new ArrayList<>());
                }
                itemOrders.get(itemForOrder.get()).add(order);
            }
        }

        StructureSellOrdersResults results = new StructureSellOrdersResults(itemOrders);
        return CompletableFuture.completedFuture(results);
    }

    @Value
    public class StructureSellOrdersResults {
        private Map<Item, List<MarketOrder>> results;
    }
}
