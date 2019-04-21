package com.github.stathvaille.marketimports.imports.configuration;

import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties
@Data
public class LocationConfiguration {

    @NotNull
    ImportLocation importDestination;

    @NotNull
    ImportLocation importSource;

    @Bean
    public ImportLocation importDestination(){
        return importDestination;
    }

    @Bean
    public ImportLocation importSource(){
        return importSource;
    }
}