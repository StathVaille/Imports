package com.github.stathvaille.marketimports.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.stathvaille.marketimports.domain.typeid.Items;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ItemConfig {

    @Bean
    public Items items() throws IOException {
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = new ClassPathResource("typeIDs.yaml").getInputStream();
        return yamlObjectMapper.readValue(inputStream, Items.class);
    }
}
