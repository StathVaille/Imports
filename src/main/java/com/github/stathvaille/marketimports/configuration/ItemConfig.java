package com.github.stathvaille.marketimports.configuration;

import com.github.stathvaille.marketimports.domain.Item;
import com.github.stathvaille.marketimports.domain.Items;
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
public class ItemConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String itemsFileLocation = "typeIDs.yaml";

    @Bean
    public Items items() throws IOException {
        logger.info("Reading item types map from " + itemsFileLocation + ". This can take a while...");
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = new ClassPathResource(itemsFileLocation).getInputStream();
        Map<Integer, Item> itemMap = yamlObjectMapper.readValue(inputStream, new TypeReference<Map<Integer, Item>>() {});

        logger.info("Read in " + itemMap.size() + " items.");
        return new Items(itemMap);
    }
}
