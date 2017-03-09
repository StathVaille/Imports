package com.github.stathvaille.marketimports.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Redirects the main page to swagger API
 */
@Controller
@RequestMapping("/")
@ApiIgnore
public class SwaggerRedirectController {

    @GetMapping
    public String redirect() {
        return "redirect:/swagger-ui.html";
    }
}
