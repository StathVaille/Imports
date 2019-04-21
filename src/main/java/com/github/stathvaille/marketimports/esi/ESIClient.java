package com.github.stathvaille.marketimports.esi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public abstract class ESIClient {

    @Autowired
    protected OAuth2AuthorizedClientService authorizedClientService;

    protected RestTemplate getOAuthRestTemplate(OAuth2AuthenticationToken authentication) throws AuthenticationException {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        if (client == null) {
            throw new SessionAuthenticationException("Not authorised with OAUTH2");
        }

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            // TODO throw a 401 when this is null
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
            return execution.execute(request, body);
        });

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

}