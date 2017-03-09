package com.github.stathvaille.marketimports.configuration;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.service.ItemService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Loads interesting items (aka items that we might want to import) from config
 */
@Configuration
@ConfigurationProperties
@Data
public class InterestingItemsConfiguration {

    List<Long> interestingItemIds;

    @Bean
    List<Item> interestingItems(@Autowired ItemService itemService){
        return interestingItemIds.stream()
                .map(itemId -> itemService.getItemById(itemId))
                .flatMap(item -> item.isPresent() ? Stream.of(item.get()) : Stream.empty())
                .collect(Collectors.toList());
    }
}
