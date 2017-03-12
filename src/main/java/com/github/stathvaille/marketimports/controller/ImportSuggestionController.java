package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.ResponseWrapper;
import com.github.stathvaille.marketimports.service.ImportSuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseWrapper<List<ImportSuggestion>> getImportSuggestions(){
        logger.info("Calculating market import suggestions...");
        List<ImportSuggestion> importSuggestions = importSuggestionService.getImportSuggestions();
        logger.info("Found " + importSuggestions.size() + " import suggestions.");
        return new ResponseWrapper(importSuggestions);
    }
}
