package com.github.stathvaille.marketimports.imports.controller;

import com.github.stathvaille.marketimports.esi.domain.MarketOrder;
import com.github.stathvaille.marketimports.esi.ESIClient;
import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.imports.service.StructureMarketsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StationMarketsController extends ESIClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired @Qualifier("importDestination") ImportLocation importDestination;

    @Autowired
    StructureMarketsService structureMarketsService;

    @RequestMapping("/api/stationOrders")
    public List<MarketOrder> stationOrders(OAuth2AuthenticationToken authentication)
    {
        return structureMarketsService.getAllSellOrdersInStructure(importDestination, authentication);
    }
}
