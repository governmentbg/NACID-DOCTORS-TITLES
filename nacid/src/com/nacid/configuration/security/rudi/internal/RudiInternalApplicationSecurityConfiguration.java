package com.nacid.configuration.security.rudi.internal;

import bg.duosoft.security.nacid.configuration.base.BaseApplicationSecurityConfiguration;
import com.nacid.web.filters.PermissionFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CsrfFilter;

/**
 * User: ggeorgiev
 * Date: 10.7.2019 г.
 * Time: 17:32
 */
@EnableWebSecurity
@Configuration
@Order(10)
public class RudiInternalApplicationSecurityConfiguration extends BaseApplicationSecurityConfiguration {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").access("isAuthenticated()")
                .and().csrf().disable()
                .httpBasic().authenticationEntryPoint(casAuthenticationEntryPoint())
                .and()
                .addFilterAt(singleSignOutFilter(), CsrfFilter.class)
                .addFilterAt(logoutFilter(), LogoutFilter.class)
                .addFilterAfter(casAuthenticationFilter(), SingleSignOutFilter.class)
                .addFilterAfter(permissionFilter(), AnonymousAuthenticationFilter.class);
    }

    @Bean("permissionFilter")
    public PermissionFilter permissionFilter() {
        return new PermissionFilter();
    }

}
