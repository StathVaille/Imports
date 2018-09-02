package com.github.stathvaille.marketimports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MarketImportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketImportsApplication.class, args);
	}
}
