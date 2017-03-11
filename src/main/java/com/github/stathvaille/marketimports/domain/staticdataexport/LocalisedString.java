package com.github.stathvaille.marketimports.domain.staticdataexport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalisedString {
    private String en;

    @Override
    public String toString(){
        return en;
    }
}
