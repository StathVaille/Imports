package com.github.stathvaille.marketimports.domain.typeid;

import lombok.Data;

import java.util.HashMap;

@Data
public class Items {
    HashMap<Integer, Item> items = new HashMap<Integer, Item>();
}
