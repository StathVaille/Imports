package com.github.stathvaille.marketimports.imports.domain.staticdataexport;

import lombok.Value;

import java.util.Map;

@Value
public class Items {
    private final Map<Long, Item> items;

    public Items(Map<Long, Item> items){
        this.items = items;
    }
}
