package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MarketSellOrderServiceTest {

    @Autowired MarketSellOrderService marketSellOrderService;

    @Autowired
    @Qualifier("importDestination")
    ImportLocation importDestination;

    @Test
    public void getItemOrders() throws Exception {
        List<MarketOrder> marketOrderList = marketSellOrderService.getItemOrders(importDestination, 608);
        assertThat(marketOrderList, notNullValue());
    }
}