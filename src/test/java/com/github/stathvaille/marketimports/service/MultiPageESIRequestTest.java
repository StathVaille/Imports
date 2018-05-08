package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.MarketImportsApplication;
import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MarketImportsApplication.class)
public class MultiPageESIRequestTest {


    @Test
    public void testMultiPageESIQuery() {
        MultiPageESIRequest multiPageESIRequest = new MultiPageESIRequest<MarketOrder>();

        final String amarrRegionId = "10000043";

        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("region_id", amarrRegionId);
        UriComponents uriComponents =
                UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("esi.evetech.net")
                        .path("/latest/markets/{region_id}/orders/")
                        .queryParam("datasource", "tranquility")
                        .queryParam("order_type", "sell")
                        .build()
                        .expand(templateParams)
                        .encode();

        ParameterizedTypeReference esiObjectType = new ParameterizedTypeReference<List<MarketOrder>>() {};
        List<MarketOrder> marketOrders = multiPageESIRequest.makeESICall(uriComponents.toUri(), esiObjectType);
        System.out.println(marketOrders.size());

        assertThat(marketOrders.size()).isGreaterThan(0);
        assertThat(marketOrders.get(0).getClass()).isEqualTo(MarketOrder.class);
    }

}

