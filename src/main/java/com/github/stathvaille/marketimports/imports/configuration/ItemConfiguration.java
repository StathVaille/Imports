package com.github.stathvaille.marketimports.imports.configuration;

import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Group;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Groups;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Items;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ItemConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Groups groups;

    @Bean
    public Items items() throws IOException {
        String itemsFileLocation = "typeIDs.yaml";
        logger.info("Reading item types map from " + itemsFileLocation + ". This can take a while...");
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = new ClassPathResource(itemsFileLocation).getInputStream();
        Map<Long, Item> itemMap = yamlObjectMapper.readValue(inputStream, new TypeReference<Map<Long, Item>>() {});

        itemMap.keySet().forEach(itemId -> itemMap.get(itemId).setTypeId(itemId));
        overrideVolumeForShips(itemMap);

        logger.info("Read in " + itemMap.size() + " items.");
        return new Items(itemMap);
    }

    private void overrideVolumeForShips(Map<Long, Item> itemMap){
        System.out.println("thing");

        Map<String, Integer> volumeOverrideMap = new HashMap<>();
        volumeOverrideMap.put("Frigate", 2500);
        volumeOverrideMap.put("Destroyer", 5000);
        volumeOverrideMap.put("Cruiser", 10000);
        volumeOverrideMap.put("Combat Battlecruiser", 15000);
        volumeOverrideMap.put("Battleship", 50000);
        volumeOverrideMap.put("Industrial", 20000);
        volumeOverrideMap.put("Shuttle", 500);
        volumeOverrideMap.put("Assault Frigate", 2500);
        volumeOverrideMap.put("Heavy Assault Cruiser", 10000);
        volumeOverrideMap.put("Deep Space Transport", 20000);
        volumeOverrideMap.put("Elite Battleship", 50000);
        volumeOverrideMap.put("Mining Barge", 3750);
        volumeOverrideMap.put("Command Ship", 15000);
        volumeOverrideMap.put("Interdictor", 5000);
        volumeOverrideMap.put("Covert Ops", 2500);
        volumeOverrideMap.put("Interceptor", 2500);
        volumeOverrideMap.put("Logistics", 10000);
        volumeOverrideMap.put("Force Recon Ship", 10000);
        volumeOverrideMap.put("Stealth Bomber", 2500);
        volumeOverrideMap.put("Electronic Attack Ship", 2500);
        volumeOverrideMap.put("Heavy Interdiction Cruiser", 10000);
        volumeOverrideMap.put("Black Ops", 50000);
        volumeOverrideMap.put("Marauder", 50000);
        volumeOverrideMap.put("Combat Recon Ship", 10000);
        volumeOverrideMap.put("Strategic Cruiser", 10000);
        volumeOverrideMap.put("Prototype Exploration Ship", 2500);
        volumeOverrideMap.put("Attack Battlecruiser", 15000);
        volumeOverrideMap.put("Blockade Runner", 20000);
        volumeOverrideMap.put("Tactical Destroyer", 5000);
        volumeOverrideMap.put("Logistics Frigate", 2500);
        volumeOverrideMap.put("Command Destroyer", 5000);

        itemMap.keySet().forEach(itemId -> {
            Item item = itemMap.get(itemId);
            Group group = groups.getGroups().get(item.getGroupID());

            if (group == null) {
                throw new RuntimeException("Could not find group for item " + item);
            }

            if (volumeOverrideMap.containsKey(group.getName().getEn())) {
                int actualItemSize = volumeOverrideMap.get(group.getName().getEn());
                logger.info("Overriding volume for " + item + " to " + actualItemSize);
                item.setVolume(actualItemSize);
            }
        });

    }
}
