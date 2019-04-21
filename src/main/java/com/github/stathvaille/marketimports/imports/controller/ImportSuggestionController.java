package com.github.stathvaille.marketimports.imports.controller;

import com.github.stathvaille.marketimports.imports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.imports.domain.ResponseWrapper;
import com.github.stathvaille.marketimports.imports.service.ImportSuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/import")
public class ImportSuggestionController {

    private final ImportSuggestionService importSuggestionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ImportSuggestionController(ImportSuggestionService importSuggestionService){
        this.importSuggestionService = importSuggestionService;
    }

    @GetMapping
    @CrossOrigin
    public ResponseWrapper<List<ImportSuggestion>> getImportSuggestions(OAuth2AuthenticationToken authentication){
        logger.info("Calculating market import suggestions...");
        List<ImportSuggestion> importSuggestions = importSuggestionService.getImportSuggestions(authentication);
        logger.info("Found " + importSuggestions.size() + " import suggestions.");
        return new ResponseWrapper(importSuggestions);
    }
}
