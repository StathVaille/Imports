package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://esi.tech.ccp.is/latest/markets/10000014/history/?datasource=tranquility&type_id=2185
 */
@Service
public class MarketHistoryService {

    private static final String apiTemplate = "https://esi.tech.ccp.is/latest/markets/%s/history/?datasource=tranquility&type_id=%s";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Double getAverageNumberOfSalesInPast7Days(Item item, ImportLocation importLocation){
        logger.info("Calculating volume of " + item.getName() + " + sold in the last 7 days in " + importLocation.getRegionName());
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(apiTemplate, importLocation.getRegionId(), item.getTypeId());
        Map[] itemHistoryArray = restTemplate.getForObject(url, HashMap[].class);
        List<Map> itemHistoryList = Arrays.asList(itemHistoryArray);

        List<Map> last7ItemHistory;
        if(itemHistoryList.size() > 7) {
            last7ItemHistory = itemHistoryList.subList(itemHistoryArray.length - 8, itemHistoryArray.length - 1);
        }
        else {
            last7ItemHistory = itemHistoryList;
        }

        List<Integer> last7ItemVolumes = last7ItemHistory.stream().map(itemHistory -> (int) itemHistory.get("volume")).collect(Collectors.toList());
        OptionalDouble optionalAverage = last7ItemVolumes.stream().mapToInt(a -> a).average();
        if (optionalAverage.isPresent()){
            return optionalAverage.getAsDouble();
        }
        else{
            return 0.0;
        }
    }

    public Map<Item, Double> getAverageNumberOfSalesInPast7Days(Collection<Item> items, ImportLocation importLocation){
        Map<Item, Double> itemAverageSalesVolume = new HashMap<>();
        items.parallelStream().forEach(item ->
                itemAverageSalesVolume.put(item, getAverageNumberOfSalesInPast7Days(item, importLocation))
        );
        return itemAverageSalesVolume;
    }

}
