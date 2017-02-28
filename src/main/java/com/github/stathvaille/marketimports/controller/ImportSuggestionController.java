package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/api/import")
public class ImportSuggestionController {

    @GetMapping
    public List<ImportSuggestion> getImportSuggestions(){
        return new ArrayList<>();
    }
}
