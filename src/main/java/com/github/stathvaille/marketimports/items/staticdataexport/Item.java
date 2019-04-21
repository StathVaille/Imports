package com.github.stathvaille.marketimports.items.staticdataexport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private long typeId;
    private int graphicID;
    private long groupID;
    private LocalisedString description;
    private LocalisedString name;
    private double volume;

    @Override
    public String toString(){
        return name.getEn();
    }
}
