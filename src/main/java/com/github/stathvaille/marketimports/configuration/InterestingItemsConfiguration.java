package com.github.stathvaille.marketimports.configuration;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.service.ItemService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Loads interesting items (aka items that we might want to import) from config
 */
@Configuration
@ConfigurationProperties
@Data
public class InterestingItemsConfiguration {

    String interestingItemsFile;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    List<Item> interestingItems(@Autowired ItemService itemService) throws IOException {

        Resource resource = new ClassPathResource("/" + interestingItemsFile);
        FileReader fileReader = new FileReader(resource.getFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<Item> items = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] splitLine = line.split(",");

            // Only look at lines with a type and a typeId
            if (splitLine.length == 2) {
                String itemName = splitLine[0];
                String typeIdString = splitLine[1];
                Long typeId = parseTypeId(typeIdString);
                Optional<Item> item = itemService.getItemById(typeId);

                if (!item.isPresent()) {
                    logger.error("Could not find type id of " + itemName + ": " + typeIdString);
                } else {
                    items.add(item.get());
                }
            }
        }

        logger.info("Loaded " + items.size() + " interesting items from " + interestingItemsFile);
        return items;
    }

    private long parseTypeId(String typeIdString) {
        try {
            return Long.parseLong(typeIdString);
        } catch (NumberFormatException e) {
            logger.error("Could not parse type id to long: " + typeIdString);
            throw new RuntimeException("Could not parse type id to long: " + typeIdString, e);
        }
    }
}
