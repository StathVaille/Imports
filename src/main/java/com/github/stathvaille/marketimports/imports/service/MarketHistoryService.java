package com.github.stathvaille.marketimports.imports.service;

import com.github.stathvaille.marketimports.esi.service.ESIClient;
import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://esi.tech.ccp.is/latest/markets/10000014/history/?datasource=tranquility&type_id=2185
 */
@Service
public class MarketHistoryService extends ESIClient {

    private static final String apiTemplate = "https://esi.evetech.net/latest/markets/%s/history/?datasource=tranquility&type_id=%s";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public Double getAverageNumberOfSalesInPast7Days(Item item, ImportLocation importLocation, OAuth2AuthenticationToken authentication){
        try{
            RestTemplate restTemplate = getOAuthRestTemplate(authentication);

            String url = String.format(apiTemplate, importLocation.getRegionId(), item.getTypeId());
            Map[] itemHistoryArray = restTemplate.getForObject(url, HashMap[].class);
            List<Map> itemHistoryList = Arrays.asList(itemHistoryArray);

            LocalDate sevenDaysAgo = LocalDate.now().minusDays(8);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            List<Map> last7ItemHistory = itemHistoryList.stream().filter(itemHistory ->  {
                LocalDate historicDate = LocalDate.parse((String)itemHistory.get("date"), formatter);
                return historicDate.isAfter(sevenDaysAgo);
            }).collect(Collectors.toList());

            List<Integer> last7ItemVolumes = last7ItemHistory.stream().map(itemHistory -> (int) itemHistory.get("volume")).collect(Collectors.toList());
            int totalSoldInLast7Days = last7ItemVolumes.stream().mapToInt(a -> a).sum();
            double averageSoldInLast7Days = totalSoldInLast7Days / 7.0;
            logger.info("Calculating number of " + item.getName() + " sold in the last 7 days at " + importLocation.getRegionName() + ": " + averageSoldInLast7Days);
            return averageSoldInLast7Days;
        }
        catch (RestClientException e){
            logger.error("Error getting market history for item " + item + ". No history will be returned", e.getMessage());
            return 0.0;
        }
    }

    public Map<Item, Double> getAverageNumberOfSalesInPast7Days(Collection<Item> items, ImportLocation importLocation, OAuth2AuthenticationToken authentication){
        Map<Item, Double> itemAverageSalesVolume = new HashMap<>();
        items.parallelStream().forEach(item ->
                itemAverageSalesVolume.put(item, getAverageNumberOfSalesInPast7Days(item, importLocation, authentication))
        );
        return itemAverageSalesVolume;
    }

}
