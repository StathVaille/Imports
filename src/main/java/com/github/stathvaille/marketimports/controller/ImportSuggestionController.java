package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.service.MarketBuyPriceService;
import com.github.stathvaille.marketimports.service.MarketSellOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@RestController("/api/import")
@Controller
@ResponseBody
@RequestMapping("/api/import")
public class ImportSuggestionController {

    @NotNull
    public final List<Item> interestingItems;
    private final MarketSellOrderService marketSellOrderService;
    private final MarketBuyPriceService marketBuyPriceService;
    private final ImportLocation importSource;
    private final ImportLocation importDestination;

    public ImportSuggestionController(@Autowired @Qualifier("interestingItems") List<Item> interestingItems,
                                      MarketSellOrderService marketSellOrderService,
                                      MarketBuyPriceService marketBuyPriceService,
                                      @Autowired @Qualifier("importSource") ImportLocation importSource,
                                      @Autowired @Qualifier("importDestination") ImportLocation importDestination) {
        this.interestingItems = interestingItems;
        this.marketSellOrderService = marketSellOrderService;
        this.marketBuyPriceService = marketBuyPriceService;
        this.importSource = importSource;
        this.importDestination = importDestination;
    }

    @GetMapping
    public List<ImportSuggestion> getImportSuggestions(){
        Map<Item, List<MarketOrder>> destinationMarketOrders = marketSellOrderService.getMultipleItemOrders(importDestination, interestingItems);
        Map<Item, Long> item5PercentBuyPrice = marketBuyPriceService.getItem5PercentBuyPrice(interestingItems);
        return new ArrayList<>();
    }
}
