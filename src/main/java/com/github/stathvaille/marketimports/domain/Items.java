package com.github.stathvaille.marketimports.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Items {
    Map<Integer, Item> items;
}
