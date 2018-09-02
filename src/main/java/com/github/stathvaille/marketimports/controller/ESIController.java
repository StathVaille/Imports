package com.github.stathvaille.marketimports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ESIController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/user")
    public Map<String, Object> getUser()
            throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof DefaultOAuth2User) {
            return ((DefaultOAuth2User) principal).getAttributes();
        }

        return new HashMap<>();
    }

    @GetMapping("location")
    public Map getMarket(OAuth2AuthenticationToken authentication, OAuth2AuthenticationToken principal) {
        Integer characterId = (Integer) principal.getPrincipal().getAttributes().get("CharacterID");
        String charLocationURI = "https://esi.evetech.net/latest/characters/" + characterId + "/location/?datasource=tranquility";
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
        HttpEntity entity = new HttpEntity("", headers);
        ResponseEntity<Map> response = restTemplate.exchange(charLocationURI, HttpMethod.GET, entity, Map.class);
        Map userAttributes = response.getBody();
        return userAttributes;
    }
}