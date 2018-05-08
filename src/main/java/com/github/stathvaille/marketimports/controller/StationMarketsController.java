package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.service.MultiPageESIRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StationMarketsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MultiPageESIRequest<MarketOrder> multiPageESIRequest = new MultiPageESIRequest<>();

    private int characterId;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired @Qualifier("importDestination") ImportLocation importDestination;

    @RequestMapping("/user")
    public Map<String, Object> user(OAuth2Authentication authentication)
    {
        Map<String, Object> details = (Map<String, Object>)authentication.getUserAuthentication().getDetails();
        characterId = (Integer)details.get("CharacterID");
        return details;
    }

    @RequestMapping("/api/stationOrders")
    public List<MarketOrder> stationOrders()
    {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(new AuthorizationCodeResourceDetails(), oauth2ClientContext);

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
        List<MarketOrder> marketOrders = multiPageESIRequest.makeESICall(uriComponents.toUri(), esiObjectType, restTemplate);
        // Remove buy orders
        marketOrders = marketOrders.stream().filter(order -> !order.is_buy_order()).collect(Collectors.toList());

        logger.info("Found " + marketOrders.size()  + " market orders in " + importDestination.getStationName());
        return marketOrders;
    }
}
