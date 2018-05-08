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
import java.util.Optional;

@Service
public class ImportSuggestionService {

    @NotNull
    public final List<Item> interestingItems;
    private final MarketSellOrderService marketSellOrderService;
    private final MarketBuyPriceService marketBuyPriceService;
    private final ImportLocation importSource;
    private final ImportLocation importDestination;
    private final Double desiredMargin;
    private final MarketHistoryService marketHistoryService;

    public ImportSuggestionService(@Autowired @Qualifier("interestingItems") List<Item> interestingItems,
                                   MarketSellOrderService marketSellOrderService,
                                   MarketBuyPriceService marketBuyPriceService,
                                   MarketHistoryService marketHistoryService,
                                   @Autowired @Qualifier("importSource") ImportLocation importSource,
                                   @Autowired @Qualifier("importDestination") ImportLocation importDestination,
                                   @Autowired @Qualifier("desiredMargin") Double desiredMargin) {
        this.interestingItems = interestingItems;
        this.marketSellOrderService = marketSellOrderService;
        this.marketBuyPriceService = marketBuyPriceService;
        this.importSource = importSource;
        this.importDestination = importDestination;
        this.desiredMargin = desiredMargin;
        this.marketHistoryService = marketHistoryService;
    }

    public List<ImportSuggestion> getImportSuggestions(){
        // TODO use Async to run these in parallel
        Map<Item, List<MarketOrder>> destinationMarketOrdersForAllItems = marketSellOrderService.getMultipleItemOrders(importDestination, interestingItems);
        Map<Item, Optional<MarketOrder>> itemsMinSellPrice = marketBuyPriceService.getMinSalesPrices(interestingItems, importSource);
        Map<Item, Double> itemVolumeHistoryInDestination = marketHistoryService.getAverageNumberOfSalesInPast7Days(itemsMinSellPrice.keySet(), importDestination);

        return buildImportSuggestions(destinationMarketOrdersForAllItems, itemsMinSellPrice, itemVolumeHistoryInDestination);
    }

    private List<ImportSuggestion> buildImportSuggestions(Map<Item, List<MarketOrder>> destinationMarketOrdersForAllItems,
                                                          Map<Item, Optional<MarketOrder>> cheapestSellOrder,
                                                          Map<Item, Double> itemVolumeHistoryInDestination) {
        List<ImportSuggestion> importSuggestions = new ArrayList<>();
        for (Item item : cheapestSellOrder.keySet()) {

            // Item
            ImportSuggestion importSuggestion = new ImportSuggestion();
            importSuggestion.setItem(item);
            importSuggestion.setNumberSoldInDestinationPerDay(itemVolumeHistoryInDestination.get(item));

            // TODO items destroyed

            // Source Prices
            Optional<MarketOrder> cheapestMarketOrder = cheapestSellOrder.get(item);
            if (cheapestMarketOrder.isPresent()) {
                importSuggestion.setMinPriceInSource(cheapestMarketOrder.get().getPrice());
                importSuggestion.setNumOnSaleAtCheapestPriceInSource(cheapestMarketOrder.get().getVolume_total());
            }
            else {
                importSuggestion.setMinPriceInSource(Double.MAX_VALUE);
                importSuggestion.setNumOnSaleAtCheapestPriceInSource(0);
            }

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
        double markedUpPrice = Math.round(importSuggestion.getMinPriceInSource() * desiredMargin);
        importSuggestion.setMinPriceInDestination(markedUpPrice);
        importSuggestion.setVolRemainingInDestination(0);
        importSuggestion.setDistinctMarketOrdersInDestination(0);
    }
}