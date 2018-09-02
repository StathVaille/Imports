package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.MarketImportsApplication;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MarketImportsApplication.class)
public class MarketBuyPriceServiceTest {

    @Autowired MarketBuyPriceService marketBuyPriceService;
    @Autowired ItemService itemService;
    @Autowired @Qualifier("importSource") ImportLocation importSource;

    @Test
    public void getMinSalesPrices() {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(itemService.getItemByName("Antimatter Charge S").get());

        Map<Item, Double> minSalesPrices = marketBuyPriceService.getMinSalesPrices(itemList, importSource);
        System.out.println("hello");
    }
}