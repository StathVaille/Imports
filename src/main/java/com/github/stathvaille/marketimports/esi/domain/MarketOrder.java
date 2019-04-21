package com.github.stathvaille.marketimports.esi.domain;

import lombok.Data;
import lombok.Value;

import java.util.Date;

@Data
public class MarketOrder {
    private long order_id;
    private long type_id;
    private long location_id;
    private long volume_total;
    private long volume_remain;
    private long min_volume;
    private double price;
    private boolean is_buy_order;
    private long duration;
    private Date issued;
    private String range;
    private long system_id;
}