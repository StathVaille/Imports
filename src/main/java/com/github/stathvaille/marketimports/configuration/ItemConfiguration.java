package com.github.stathvaille.marketimports.configuration;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.domain.staticdataexport.Items;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration
public class ItemConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public Items items() throws IOException {
        String itemsFileLocation = "typeIDs.yaml";
        logger.info("Reading item types map from " + itemsFileLocation + ". This can take a while...");
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = new ClassPathResource(itemsFileLocation).getInputStream();
        Map<Long, Item> itemMap = yamlObjectMapper.readValue(inputStream, new TypeReference<Map<Long, Item>>() {});

        itemMap.keySet().forEach(itemId -> itemMap.get(itemId).setTypeId(itemId));

        logger.info("Read in " + itemMap.size() + " items.");
        return new Items(itemMap);
    }
}
