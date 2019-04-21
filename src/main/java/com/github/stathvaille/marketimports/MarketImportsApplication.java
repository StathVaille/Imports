package com.github.stathvaille.marketimports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAsync
public class MarketImportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketImportsApplication.class, args);
	}
}
