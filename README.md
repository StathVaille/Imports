# Market Imports

## Updating Static Date

item and group yaml files...

## Registering oauth with CCP

## Algorithm

1. Calculate buy price from import source using *MarketBuyPriceService.getMinSalesPrices()*
2. Calculate market demand in import destination using *marketHistoryService.getAverageNumberOfSalesInPast7Days()*
3. Calculate volume on sale in destination using *structureMarketsService.getAllSellOrdersInStructure()*