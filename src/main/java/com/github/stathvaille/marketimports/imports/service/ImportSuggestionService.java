package com.github.stathvaille.marketimports.imports.service;

import com.github.stathvaille.marketimports.imports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.esi.domain.MarketOrder;
import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.items.staticdataexport.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ImportSuggestionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @NotNull
    public final List<Item> interestingItems;
    private final MarketBuyPriceService marketBuyPriceService;
    private final StructureMarketsService structureMarketsService;
    private final ImportLocation importSource;
    private final ImportLocation importDestination;
    private final Double desiredMargin;
    private final MarketHistoryService marketHistoryService;

    public ImportSuggestionService(@Autowired @Qualifier("interestingItems") List<Item> interestingItems,
                                   MarketBuyPriceService marketBuyPriceService,
                                   MarketHistoryService marketHistoryService,
                                   StructureMarketsService structureMarketsService,
                                   @Autowired @Qualifier("importSource") ImportLocation importSource,
                                   @Autowired @Qualifier("importDestination") ImportLocation importDestination,
                                   @Autowired @Qualifier("desiredMargin") Double desiredMargin) {
        this.interestingItems = interestingItems;
        this.marketBuyPriceService = marketBuyPriceService;
        this.structureMarketsService = structureMarketsService;
        this.importSource = importSource;
        this.importDestination = importDestination;
        this.desiredMargin = desiredMargin;
        this.marketHistoryService = marketHistoryService;
    }

    public List<ImportSuggestion> getImportSuggestions(OAuth2AuthenticationToken authentication){
        long startTime = System.currentTimeMillis();

        // TODO use Async to run these in parallel
        Map<Item, Optional<MarketOrder>> itemsMinSellPrice = marketBuyPriceService.getMinSalesPrices(interestingItems, importSource, authentication);
        Map<Item, Double> itemVolumeHistoryInDestination = marketHistoryService.getAverageNumberOfSalesInPast7Days(itemsMinSellPrice.keySet(), importDestination, authentication);
        Map<Item, List<MarketOrder>> destinationMarketOrdersForAllItems = structureMarketsService.getAllSellOrdersInStructure(importDestination, interestingItems, authentication);
        List<ImportSuggestion> importSuggestions = buildImportSuggestions(destinationMarketOrdersForAllItems, itemsMinSellPrice, itemVolumeHistoryInDestination);
        long processingTimeSeconds = (System.currentTimeMillis() - startTime) / 1000;
        logger.info("Calculated import suggestions in {} seconds", processingTimeSeconds);

        return importSuggestions;
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