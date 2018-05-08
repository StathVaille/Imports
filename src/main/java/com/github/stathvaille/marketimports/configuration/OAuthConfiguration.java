package com.github.stathvaille.marketimports.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableOAuth2Client
public class OAuthConfiguration extends WebSecurityConfigurerAdapter {

    // The url on the client app where the user goes to log in. Springs OAuth magic will redirect the client to the eve
    // OAuth login url (specified in application properties as "userAuthorizationUri"). The Eve site will then redirect the
    // browser back to the same login URL with an authorization code . At this point "spring magic" will validate the code at
    // the EVE validation URL (specified in application.properties as "accessTokenUri"). At this point the user is authenticated
    // and everything works.
    @Value("${uris.login}")
    private  String loginUrl;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Override
    protected void configure(HttpSecurity http)
            throws Exception
    {
        // @formatter:off
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().csrfTokenRepository(csrfTokenRepository())
                .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
        // @formatter:on
    }

    private Filter csrfHeaderFilter()
    {
        return new OncePerRequestFilter()
        {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain)
                    throws ServletException, IOException
            {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if(csrf != null)
                {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if(cookie == null || token != null && !token.equals(cookie.getValue()))
                    {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository()
    {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    @ConfigurationProperties("eve.client")
    protected OAuth2ProtectedResourceDetails eve()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("eve.resource")
    protected ResourceServerProperties eveResource()
    {
        return new ResourceServerProperties();
    }

    @Bean
    protected FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter ssoFilter()
    {
        OAuth2ClientAuthenticationProcessingFilter eveFilter = new OAuth2ClientAuthenticationProcessingFilter(loginUrl);
        OAuth2RestTemplate eveTemplate = new OAuth2RestTemplate(eve(), oauth2ClientContext);
        eveFilter.setRestTemplate(eveTemplate);
        eveFilter.setTokenServices(new UserInfoTokenServices(eveResource().getUserInfoUri(), eve().getClientId()));  // validate OAuth access token
        return eveFilter;
    }
}