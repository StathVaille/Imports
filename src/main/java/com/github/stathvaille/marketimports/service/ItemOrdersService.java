package com.github.stathvaille.marketimports.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by stath on 21/08/2016.
 */
@Service
public class ItemOrdersService {

    //private static String url = "http://eve-marketdata.com/api/item_orders2.json?char_name=stath_vaille&station_ids=" + stationId + "&buysell=s";

    public String getItemOrders(){
        RestTemplate restTemplate = new RestTemplate();

        return "";
    }
}
