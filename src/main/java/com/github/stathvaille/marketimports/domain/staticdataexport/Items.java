package com.github.stathvaille.marketimports.domain.staticdataexport;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;

import java.util.Map;

public class Items {
    private final Map<Integer, Item> items;

    public Items(Map<Integer, Item> items){
        this.items = items;
    }
}
