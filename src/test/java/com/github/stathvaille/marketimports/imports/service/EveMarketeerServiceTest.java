package com.github.stathvaille.marketimports.imports.service;

import com.github.stathvaille.marketimports.imports.domain.EveMarketeerValuation;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.killmails.EveMarketeerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EveMarketeerServiceTest {

    @Autowired
    EveMarketeerService eveMarketeerService;
    @Autowired ItemService itemService;

    @Test
    public void getItemPrice() {
        Item item = itemService.getItemByName("Caldari Navy Antimatter Charge S").get();
        EveMarketeerValuation itemPrice = eveMarketeerService.getItemPrice(item);
        assert  itemPrice != null;
    }
}