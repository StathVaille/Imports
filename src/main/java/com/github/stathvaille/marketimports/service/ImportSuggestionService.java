package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImportSuggestionService {

    @NotNull
    public final List<Item> interestingItems;
    private final MarketSellOrderService marketSellOrderService;
    private final MarketBuyPriceService marketBuyPriceService;
    private final ImportLocation importSource;
    private final ImportLocation importDestination;
    private final Double desiredMargin;

    public ImportSuggestionService(@Autowired @Qualifier("interestingItems") List<Item> interestingItems,
                                   MarketSellOrderService marketSellOrderService,
                                   MarketBuyPriceService marketBuyPriceService,
                                   @Autowired @Qualifier("importSource") ImportLocation importSource,
                                   @Autowired @Qualifier("importDestination") ImportLocation importDestination,
                                   @Autowired @Qualifier("desiredMargin") Double desiredMargin) {
        this.interestingItems = interestingItems;
        this.marketSellOrderService = marketSellOrderService;
        this.marketBuyPriceService = marketBuyPriceService;
        this.importSource = importSource;
        this.importDestination = importDestination;
        this.desiredMargin = desiredMargin;
    }

    public List<ImportSuggestion> getImportSuggestions(){
        Map<Item, List<MarketOrder>> destinationMarketOrdersForAllItems = marketSellOrderService.getMultipleItemOrders(importDestination, interestingItems);
        Map<Item, Double> item5PercentBuyPrice = marketBuyPriceService.getItem5PercentBuyPrice(interestingItems, importSource);

        List<ImportSuggestion> importSuggestions = new ArrayList<>();
        for (Item item : item5PercentBuyPrice.keySet()) {

            // Item
            ImportSuggestion importSuggestion = new ImportSuggestion();
            importSuggestion.setItem(item);

            // TODO item destroyed

            // Source Prices
            importSuggestion.setMinPriceInSource(item5PercentBuyPrice.get(item));

            // Destination Prices
            List<MarketOrder> destinationMarketOrders = destinationMarketOrdersForAllItems.get(item);
            if(destinationMarketOrders != null){
                calculateImportPricesForItem(importSuggestion, destinationMarketOrders);
            }
            else {
                calculateImportPricesForItemNotInDesignation(importSuggestion);
            }

            // Locations
            importSuggestion.setImportSource(importSource);
            importSuggestion.setImportDestination(importDestination);

            importSuggestions.add(importSuggestion);
        }

        return importSuggestions;
    }

    private void calculateImportPricesForItem(ImportSuggestion importSuggestion, List<MarketOrder> destinationMarketOrders) {
        importSuggestion.setVolRemainingInDestination(destinationMarketOrders.stream()
                .mapToLong(MarketOrder::getVolume_remain)
                .sum());
        importSuggestion.setMinPriceInDestination(destinationMarketOrders.stream()
                .mapToDouble(MarketOrder::getPrice)
                .min()
                .getAsDouble());
        importSuggestion.setDistinctMarketOrdersInDestination(destinationMarketOrders.size());
    }

    private void calculateImportPricesForItemNotInDesignation(ImportSuggestion importSuggestion) {
        importSuggestion.setMinPriceInDestination(importSuggestion.getMinPriceInSource() * desiredMargin);
        importSuggestion.setVolRemainingInDestination(0);
        importSuggestion.setDistinctMarketOrdersInDestination(0);
    }
}
