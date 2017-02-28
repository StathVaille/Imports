package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class JunkController {

    @Autowired
    Items items;

    @GetMapping
    public Items getThing(){
        return items;
    }
}
