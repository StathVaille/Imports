package com.github.stathvaille.marketimports.killmails;

import com.github.stathvaille.marketimports.imports.domain.EveMarketeerValuation;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EveMarketeerService {

    private String itemPriceEndpointTemplate = "https://api.evemarketer.com/ec/marketstat?usesystem=30000142&typeid=%s";

    RestTemplate restTemplate = new RestTemplate();

    public EveMarketeerValuation getItemPrice(Item item) {
        // Should be working. Cloudfialr does not like my browser so is giving 403 errors :(

        String itemPriceEndpoint = String.format(itemPriceEndpointTemplate, item.getTypeId());
        ResponseEntity<EveMarketeerValuation> response = restTemplate.getForEntity(itemPriceEndpoint, EveMarketeerValuation.class);

        return response.getBody();
    }
}
