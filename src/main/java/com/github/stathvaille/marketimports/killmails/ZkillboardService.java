package com.github.stathvaille.marketimports.killmails;

import com.github.stathvaille.marketimports.imports.domain.ZkilboardKillmail;
import com.github.stathvaille.marketimports.esi.domain.ESIKillmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ZkillboardService {

    RestTemplate restTemplate = new RestTemplate();

    private static String killmailsEndpointTemplate = "https://zkillboard.com/api/losses/allianceID/%s/page/%d/";

    private static String esKillmailEndpointTemplate = "https://esi.evetech.net/latest/killmails/%s/%s/?datasource=tranquility";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int numPagesToLoad = 5;

    public List<ZkilboardKillmail> loadAllianceKillmails(int allianceId) {

        List<ZkilboardKillmail> killmails = Collections.synchronizedList(new ArrayList<>());
        IntStream.rangeClosed(1, numPagesToLoad)
                .parallel()
                .forEach( pageNum -> killmails.addAll(loadAllianceKillmails(allianceId, pageNum)));

        return killmails;
    }

    public List<ZkilboardKillmail> loadAllianceKillmails(int allianceId, int page) {
        logger.info(String.format("Loading page %d of Zkillboard...", page));
        String killmailsEndpoint = String.format(killmailsEndpointTemplate, allianceId, page);
        ParameterizedTypeReference objectType = new ParameterizedTypeReference<List<ZkilboardKillmail>>() {};
        MultiValueMap<String, String> requestHeaders = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(requestHeaders); // for request
        HttpEntity<List<ZkilboardKillmail>> response = restTemplate.exchange(killmailsEndpoint, HttpMethod.GET, entity, objectType);

        List<ZkilboardKillmail> killmails = response.getBody();
        logger.info(String.format("loaded page %d of Zkillboard complete. Loaded %d killmails", page, killmails.size()));
        return killmails;
    }

    public ESIKillmail esiLookupKillmaail(String killmailHash, int killmailId) {
        String esiKillmailEndpoint = String.format(esKillmailEndpointTemplate, killmailId, killmailHash);
        ResponseEntity<ESIKillmail> esiKillmail = restTemplate.getForEntity(esiKillmailEndpoint, ESIKillmail.class);

        return esiKillmail.getBody();
    }
}
