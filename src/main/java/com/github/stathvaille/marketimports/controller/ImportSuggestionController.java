package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.ResponseWrapper;
import com.github.stathvaille.marketimports.service.ImportSuggestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@RestController("/api/import")
@Controller
@ResponseBody
@RequestMapping("/api/import")
public class ImportSuggestionController {

    private final ImportSuggestionService importSuggestionService;

    public ImportSuggestionController(ImportSuggestionService importSuggestionService){
        this.importSuggestionService = importSuggestionService;
    }

    @GetMapping
    @CrossOrigin
    public ResponseWrapper<List<ImportSuggestion>> getImportSuggestions(){
        return new ResponseWrapper(importSuggestionService.getImportSuggestions());
    }
}
