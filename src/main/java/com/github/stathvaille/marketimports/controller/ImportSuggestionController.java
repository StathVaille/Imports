package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.ImportSuggestion;
import com.github.stathvaille.marketimports.domain.esi.MarketOrder;
import com.github.stathvaille.marketimports.domain.location.ImportLocation;
import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.service.ImportSuggestionService;
import com.github.stathvaille.marketimports.service.MarketBuyPriceService;
import com.github.stathvaille.marketimports.service.MarketSellOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<ImportSuggestion> getImportSuggestions(){
        return importSuggestionService.getImportSuggestions();
    }
}
