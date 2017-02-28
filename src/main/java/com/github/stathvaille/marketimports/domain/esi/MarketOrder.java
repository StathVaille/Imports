package com.github.stathvaille.marketimports.domain.esi;

import lombok.Value;

import java.util.Date;

@Value
public class MarketOrder {
    private long order_id;
    private long type_id;
    private long location_id;
    private long volume_total;
    private long volume_remain;
    private long min_volume;
    private long price;
    private boolean is_buy_order;
    private long duration;
    private Date issued;
    private String range;
}
