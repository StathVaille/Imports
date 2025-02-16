package com.github.stathvaille.marketimports.esi;

import com.github.stathvaille.marketimports.esi.domain.EveUser;
import com.github.stathvaille.marketimports.esi.domain.CharacterLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MiscESIController {

    @Autowired
    MiscESIService miscESIService;

    @GetMapping("/user")
    public EveUser getUser() throws IOException {
        return miscESIService.getUser();
    }

    @GetMapping("/location")
    public CharacterLocation getUserLocation(OAuth2AuthenticationToken authentication) {
        return miscESIService.getUserLocation(authentication);
    }
}
