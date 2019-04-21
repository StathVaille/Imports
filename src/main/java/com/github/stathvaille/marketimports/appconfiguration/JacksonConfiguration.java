package com.github.stathvaille.marketimports.appconfiguration;

import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
    }
}
