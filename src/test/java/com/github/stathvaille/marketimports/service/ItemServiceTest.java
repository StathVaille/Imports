package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.domain.staticdataexport.Items;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ItemServiceTest {

    @Autowired Items items;
    @Autowired ItemService itemService;

    @Test
    public void itemsLoadedTest() throws Exception {
        assertThat(items, notNullValue());
    }

    @Test
    public void getItemByNameTest() {
        Optional<Item> item  = itemService.getItemByName("Atron");
        assertThat(item.isPresent(), is(true));
        assertThat(item.get().getTypeId(), equalTo(608L));
        assertThat(item.get().getName().getEn(), equalTo("Atron"));
    }

    @Test
    public void getMissingItemTest() {
        Optional<Item> item  = itemService.getItemByName("Not an Atron");
        assertThat(item.isPresent(), is(false));
    }
}