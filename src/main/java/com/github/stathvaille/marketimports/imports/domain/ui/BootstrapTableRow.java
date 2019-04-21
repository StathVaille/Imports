package com.github.stathvaille.marketimports.imports.domain.ui;

import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import lombok.Data;

@Data
public class BootstrapTableRow {
    String itemName;
    int distinctMarketOrdersInDestination;
    Item item;
    double buyPrice;
    double salePrice;
    long volRemainingInDestination;
    double numberSoldInDestinationPerDay;

    double margin;
    double profitPerItem;
    double profitPerDay;
}
