package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ui.BootstrapTableData;
import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.ui.BootstrapTableRow;
import com.github.stathvaille.marketimports.service.ImportSuggestionService;
import com.github.stathvaille.marketimports.service.MiscESIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class UIController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MiscESIService miscESIService;

    @GetMapping
    String getView(Model model, OAuth2AuthenticationToken authentication){
        model.addAttribute("eveUser", miscESIService.getUser());
        return "importSuggestions";
    }


}
