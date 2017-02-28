package com.github.stathvaille.marketimports.domain;

import com.github.stathvaille.marketimports.configuration.LocationConfiguration;
import lombok.Value;
import java.util.Date;

@Value
public class ImportSuggestion {
    String typeId;
    String name;
    double volume;
    int numLost;
    long totalIskLost;
    long itemsLostInDestination;
    long minPriceInDestination;
    long volRemainingInDestination;
    int distinctMarketOrdersInDestination;
    long minPriceInSource;
    Date lastUpdatedInSource;
    LocationConfiguration importSource;
    LocationConfiguration importDestination;
    double margin;
}