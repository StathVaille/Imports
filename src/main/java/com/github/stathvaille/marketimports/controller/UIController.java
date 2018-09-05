package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.service.MiscESIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UIController {

    @Autowired
    MiscESIService miscESIService;

    @GetMapping
    String getView(Model model, OAuth2AuthenticationToken authentication){

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = auth.getPrincipal();
//
//        String user = "Unknown";
//        if (principal instanceof DefaultOAuth2User) {
//            user = ((DefaultOAuth2User) principal).getName();
//        }

//        model.addAttribute("charName", user);
        model.addAttribute("eveUser", miscESIService.getUser());
        return "importSuggestions";
    }
}
