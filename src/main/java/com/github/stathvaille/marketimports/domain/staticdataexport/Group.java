package com.github.stathvaille.marketimports.domain.staticdataexport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {
    private long groupID;
    private int iconID;
    private LocalisedString name;

    @Override
    public String toString(){
        return name.getEn();
    }
}
