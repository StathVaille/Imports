package com.github.stathvaille.marketimports.ui.domain;

import com.github.stathvaille.marketimports.items.staticdataexport.Item;
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
