package com.github.stathvaille.marketimports.domain.typeid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalisedString {
    private String en;
}
