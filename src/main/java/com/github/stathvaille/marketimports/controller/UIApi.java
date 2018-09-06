package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.ui.BootstrapTableData;
import com.github.stathvaille.marketimports.domain.ui.BootstrapTableRow;
import com.github.stathvaille.marketimports.service.ImportSuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest endpoints specific to the UI
 */
@RestController()
public class UIApi {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ImportSuggestionService importSuggestionService;

    @Autowired @Qualifier("importDestination") ImportLocation importDestination;
    @Autowired @Qualifier("importSource") ImportLocation importSource;

    @GetMapping("/ui/hello")
    public String sayHello() {
        return "hello";
    }

    // TODO add a cache here...
    @GetMapping("/ui/imports")
    public List<BootstrapTableRow> getFormattedImportSuggestions(OAuth2AuthenticationToken authentication){
        logger.info("Calculating market import suggestions...");
        List<ImportSuggestion> importSuggestions = importSuggestionService.getImportSuggestions(authentication);

        BootstrapTableData results = new BootstrapTableData();
        List<BootstrapTableRow> rows = importSuggestions.stream().map(this::convertToRow).collect(Collectors.toList());
        results.setTotal(importSuggestions.size());
        results.setRows(rows);
        return rows;
//        return results;
    }

    @GetMapping("/ui/importSource")
    public ImportLocation getImportSource(OAuth2AuthenticationToken authentication){
        logger.info("Getting importing from...");
        return importSource;
    }

    @GetMapping("/ui/importDestination")
    public ImportLocation getImportDestination(OAuth2AuthenticationToken authentication){
        logger.info("Getting importing to...");
        return importDestination;
    }

    private BootstrapTableRow convertToRow(ImportSuggestion importSuggestion) {
        BootstrapTableRow row = new BootstrapTableRow();
        row.setItemName(importSuggestion.getItem().getName().getEn());
        row.setDistinctMarketOrdersInDestination(importSuggestion.getDistinctMarketOrdersInDestination());
        row.setItem(importSuggestion.getItem());
        row.setBuyPrice(importSuggestion.getMinPriceInSource());
        row.setSalePrice(importSuggestion.getMinPriceInDestination());
        row.setMargin(importSuggestion.getMargin());
        row.setProfitPerItem(importSuggestion.getProfitPerItem());
        row.setVolRemainingInDestination(importSuggestion.getVolRemainingInDestination());
        row.setNumberSoldInDestinationPerDay(importSuggestion.getNumberSoldInDestinationPerDay());
        row.setProfitPerDay(importSuggestion.getProfitPerDay());
        return row;
    }



}
