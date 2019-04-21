package com.github.stathvaille.marketimports.imports.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZkilboardKillmail {
    @JsonProperty("killmail_id") private int killmailId;
    private Zkb zkb;

    @Data
    public class Zkb {
        private int locationId;
        private String hash;
    }
}
