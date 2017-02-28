package com.github.stathvaille.marketimports.domain.staticdataexport;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;

import java.util.Map;

public class Items {
    private final Map<Long, Item> items;

    public Items(Map<Long, Item> items){
        this.items = items;
    }
}
