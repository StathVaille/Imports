package com.github.stathvaille.marketimports.imports.domain;

import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import lombok.Data;

@Data
public class ImportSuggestion {
    Item item;

    int numLost;
    long totalIskLost;
    long itemsLostInDestination;

    double minPriceInSource;
    long numOnSaleAtCheapestPriceInSource;
    double minPriceInDestination;
    long volRemainingInDestination;
    int distinctMarketOrdersInDestination;

    double numberSoldInDestinationPerDay;

    ImportLocation importSource;
    ImportLocation importDestination;

    public Double getProfitPerItem() {
        return minPriceInDestination - minPriceInSource;
    }

    public Double getMargin(){
        return minPriceInDestination / minPriceInSource;
    }

    public Double getProfitPerDay(){
        Double profit = getProfitPerItem() * numberSoldInDestinationPerDay;
        return Math.max(0, profit);
    }
}