package com.github.stathvaille.marketimports.esi;

import com.github.stathvaille.marketimports.esi.domain.EveUser;
import com.github.stathvaille.marketimports.esi.domain.CharacterLocation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MiscESIService extends ESIClient {

    public EveUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        EveUser eveUser = new EveUser();
        if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauthUser = ((DefaultOAuth2User) principal);
            eveUser.setCharacterName((String) oauthUser.getAttributes().get("CharacterName"));
            eveUser.setCharacterId((Integer) oauthUser.getAttributes().get("CharacterID"));
        }
        return eveUser;
    }

    public CharacterLocation getUserLocation(OAuth2AuthenticationToken authentication) {
        Integer characterId = (Integer) authentication.getPrincipal().getAttributes().get("CharacterID");
        String charLocationURI = "https://esi.evetech.net/latest/characters/" + characterId + "/location/?datasource=tranquility";

        RestTemplate restTemplate = getOAuthRestTemplate(authentication);
        ResponseEntity<CharacterLocation> response = restTemplate.getForEntity(charLocationURI, CharacterLocation.class);

        return response.getBody();
    }
}
