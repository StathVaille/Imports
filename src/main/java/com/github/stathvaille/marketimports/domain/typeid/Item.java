package com.github.stathvaille.marketimports.domain.typeid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private int graphicID;
    private int groupID;
    private LocalisedString description;
    private LocalisedString name;
    private double volume;
}
