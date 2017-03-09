package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.service.MarketSellOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@RestController("/api/import")
@Controller
@RequestMapping("/api/import")
public class ImportSuggestionController {

    @NotNull
    public final List<Item> interestingItems;
    private final MarketSellOrderService marketSellOrderService;
    private final ImportLocation importSource;
    private final ImportLocation importDestination;

    public ImportSuggestionController(@Autowired @Qualifier("interestingItems") List<Item> interestingItems,
                                      @Autowired MarketSellOrderService marketSellOrderService,
                                      @Autowired @Qualifier("importSource") ImportLocation importSource,
                                      @Autowired @Qualifier("importDestination") ImportLocation importDestination) {
        this.interestingItems = interestingItems;
        this.marketSellOrderService = marketSellOrderService;
        this.importSource = importSource;
        this.importDestination = importDestination;
    }

    @GetMapping
    public List<ImportSuggestion> getImportSuggestions(){
        Map<Long, List<MarketOrder>> destinationMarketOrders = marketSellOrderService.getMultipleItemOrders(importDestination, interestingItems);
        return new ArrayList<>();
    }
}
