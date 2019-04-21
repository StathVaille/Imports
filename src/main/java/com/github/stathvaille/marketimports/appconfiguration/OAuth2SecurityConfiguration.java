package com.github.stathvaille.marketimports.appconfiguration;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //  Using this guide: https://www.baeldung.com/spring-security-5-oauth2-login

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
    }
}
