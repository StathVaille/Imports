package com.github.stathvaille.marketimports.domain;

import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import lombok.Data;

@Data
public class ImportSuggestion {
    Item item;

    int numLost;
    long totalIskLost;
    long itemsLostInDestination;

    Double minPriceInDestination;
    long volRemainingInDestination;
    int distinctMarketOrdersInDestination;

    Double minPriceInSource;

    ImportLocation importSource;
    ImportLocation importDestination;
}