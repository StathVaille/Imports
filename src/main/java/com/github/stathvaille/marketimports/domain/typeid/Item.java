package com.github.stathvaille.marketimports.domain.typeid;

import lombok.Data;

@Data
public class Item {
    private int graphicID;
    private int groupID;
    private LocalisedString description;
    private LocalisedString name;
    private double volume;
}
