package com.github.stathvaille.marketimports.esi.domain;

import lombok.Data;

@Data
public class ESIKillmailItem {
    private long item_type_id;
    private long quantity_destroyed;
    private long quantity_dropped;
}
