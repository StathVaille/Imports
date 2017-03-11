package com.github.stathvaille.marketimports.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties
public class ImportConfiguration {

    @NotNull
    private Double desiredMargin;

    /**
     * The desired margin is the goal margin items will be impoorted at if there are not already items on the destination market
     */
    @Bean("desiredMargin")
    public Double desiredMargin(){
        return desiredMargin;
    }
}
