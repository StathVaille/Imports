package com.github.stathvaille.marketimports.items.staticdataexport;

import lombok.Value;

import java.util.Map;

@Value
public class Groups {
    private final Map<Long, Group> groups;

    public Groups(Map<Long, Group> groups){
        this.groups = groups;
    }
}
