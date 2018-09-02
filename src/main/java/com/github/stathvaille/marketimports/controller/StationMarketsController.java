package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.service.MultiPageESIRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StationMarketsController extends BaseESIController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MultiPageESIRequest<MarketOrder> multiPageESIRequest = new MultiPageESIRequest<>();

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired @Qualifier("importDestination") ImportLocation importDestination;

    @RequestMapping("/api/stationOrders")
    public List<MarketOrder> stationOrders(OAuth2AuthenticationToken authentication)
    {
        RestTemplate resttemplate = getOAuthRestTemplate(authentication);

        // Query market structures
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("structure_id", Long.toString(importDestination.getStationId()));
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
        marketOrders = marketOrders.stream().filter(order -> !order.is_buy_order()).collect(Collectors.toList());

        logger.info("Found " + marketOrders.size()  + " market orders in " + importDestination.getStationName());
        return marketOrders;
    }
}
