//package com.github.stathvaille.marketimports.imports.service;
//
//import com.github.stathvaille.marketimports.esi.domain.MarketOrder;
//import com.github.stathvaille.marketimports.imports.domain.location.ImportLocation;
//import com.github.stathvaille.marketimports.items.staticdataexport.Item;
//import com.github.stathvaille.marketimports.items.staticdataexport.LocalisedString;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static org.hamcrest.CoreMatchers.hasItems;
//import static org.hamcrest.CoreMatchers.notNullValue;
//import static org.junit.Assert.assertThat;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//public class MarketSellOrderServiceTest {
//
//    @Autowired MarketSellOrderService marketSellOrderService;
//
//    @Autowired
//    @Qualifier("importDestination")
//    ImportLocation importDestination;
//
//    Item atron;
//    Item maulus;
//
//    @Before
//    public void setup(){
//        atron = createItem(608L, "atron");
//        maulus = createItem(609L, "maulus");
//    }
//
//    private Item createItem(Long itemId, String name){
//        Item item = new Item();
//        item.setTypeId(itemId);
//        LocalisedString itemName = new LocalisedString();
//        itemName.setEn(name);
//        item.setName(itemName);
//        return item;
//    }
//
//    @Test
//    public void getItemOrders() throws Exception {
//        List<MarketOrder> marketOrderList = marketSellOrderService.getItemOrders(importDestination, atron);
//        assertThat(marketOrderList, notNullValue());
//    }
//
//    @Test
//    public void getMultipleItemOrders() throws Exception {
//
//        List<Item> items = Arrays.asList(atron, maulus);
//        Map<Item, List<MarketOrder>> marketOrderList = marketSellOrderService.getMultipleItemOrders(importDestination, items);
//
//        assertThat(marketOrderList, notNullValue());
//        assertThat(marketOrderList.keySet()
//                .stream()
//                .map(item -> item.getTypeId())
//                .collect(Collectors.toList()), hasItems(608L, 609L));
//    }
//
//}