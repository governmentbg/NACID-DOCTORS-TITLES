package com.nacid.configuration.security.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * User: ggeorgiev
 * Date: 10.07.2019
 * Time: 15:05
 */
@EnableWebSecurity
@Configuration
@Order(1)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String REST_USER = "rest";
    public static final String REST_PASS = "N@c1dR3st";
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/rest/**").httpBasic().and()
                .authorizeRequests()
                .antMatchers("/rest/**").hasRole("USER");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser(REST_USER)
                .password(REST_PASS)
                .roles("USER");
    }

}
