package com.github.stathvaille.marketimports.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.stathvaille.marketimports.items.staticdataexport.Group;
import com.github.stathvaille.marketimports.items.staticdataexport.Groups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration
public class GroupConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public Groups groups() throws IOException {
        String groupsFileLocation = "groupIDs.yaml";
        logger.info("Reading item groups map from " + groupsFileLocation + ". This can take a while...");
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = new ClassPathResource(groupsFileLocation).getInputStream();
        Map<Long, Group> groupMap = yamlObjectMapper.readValue(inputStream, new TypeReference<Map<Long, Group>>() {});

        groupMap.keySet().forEach(groupId -> groupMap.get(groupId).setGroupID(groupId));

        logger.info("Read in " + groupMap.size() + " groups.");
        return new Groups(groupMap);
    }
}
