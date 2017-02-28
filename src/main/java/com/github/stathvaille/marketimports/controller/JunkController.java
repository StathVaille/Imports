package com.github.stathvaille.marketimports.controller;

import com.github.stathvaille.marketimports.domain.typeid.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class JunkController {

    @Autowired
    Items items;

    @GetMapping
    public Items getThing(){
        return items;
    }
}
