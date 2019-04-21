package com.github.stathvaille.marketimports.esi.domain;

import lombok.Data;

import java.util.List;

@Data
public class ESIKillmail {
    private int killmail_id;
    private String killmail_time;
    private Victim victim;

    @Data
    public class Victim {
        private long alliance_id;
         private long character_id;
         private long corporation_id;
         private long ship_type_id;
         List<ESIKillmailItem> items;
    }
}
