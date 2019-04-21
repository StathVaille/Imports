package com.github.stathvaille.marketimports.esi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility code for handling ESI calls which may have multiple pages of response
 */
public class MultiPageESIRequest<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<T> makeESICall(URI uri, ParameterizedTypeReference esiObjectType, RestTemplate restTemplate){
        try{
            // First get headers to figure out how many pages there are
            HttpHeaders responseHeader = restTemplate.headForHeaders(uri);
            int numberOfPages = Integer.parseInt(responseHeader.getFirst("X-Pages"));
            logger.info(String.format("There are %d pages for endpoint: %s", numberOfPages, uri));


            List<T> allResults = IntStream.rangeClosed(1, numberOfPages)
                    .boxed()
                    .parallel()
                    .map(page -> getForPage(page, numberOfPages, uri, esiObjectType, restTemplate))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            return allResults;
        }
        catch (RestClientException e){
            logger.error("Error calling API getting", e.getMessage());
            throw new RuntimeException("Could not make mulit page ESI call", e);
        }
    }

    private List<T> getForPage(int pageNumber, int totalNumberOfPages, URI uri, ParameterizedTypeReference esiObjectType, RestTemplate restTemplate){
        URI pageUri = UriComponentsBuilder.fromUri(uri).queryParam("page", pageNumber).build().toUri();
        logger.info(String.format("Getting page %d/%d at endpoint: %s", pageNumber, totalNumberOfPages, pageUri.toString()));
        MultiValueMap<String, String> requestHeaders = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(requestHeaders); // for request
        HttpEntity<List<T>> response = restTemplate.exchange(pageUri, HttpMethod.GET, entity, esiObjectType);
        return response.getBody();
    }
}
