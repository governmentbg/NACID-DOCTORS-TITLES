package bg.duosoft.security.nacid.configuration.base;

import bg.duosoft.security.nacid.service.LiferayUserService;
import bg.duosoft.security.nacid.service.PermissionsService;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.util.Arrays;

/**
 * User: ggeorgiev
 * Date: 10.7.2019 г.
 * Time: 17:32
 */
public class BaseApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${cas.service.url}")
    private String casServiceUrl;
    @Value("${cas.login.url}")
    private String casLoginUrl;
    @Value("${cas.logout.url}")
    private String casLogoutUrl;
    @Value("${cas.application.logout.page}")
    private String casApplicationLogoutPage;
    @Value("${cas.server}")
    private String casServerUrl;

    @Bean("casAuthenticationFilter")
    public CasAuthenticationFilter casAuthenticationFilter() {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/j_spring_cas_security_check");
        return filter;
    }

    @Bean("delegateAuthenticationManager")
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(delegateAuthenticationProvider()));
    }

    @Bean("delegateAuthenticationProvider")
    public CasAuthenticationProvider delegateAuthenticationProvider() {
        CasAuthenticationProvider res = new CasAuthenticationProvider();
        res.setUserDetailsService(liferayUserService());
        res.setServiceProperties(serviceProperties());
        Cas20ServiceTicketValidator validator = new Cas20ServiceTicketValidator(casServerUrl);
        res.setTicketValidator(validator);
        res.setKey("CAS_ADAPTER");
        return res;
    }

    @Bean("delegateSecurityEntryPoint")
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint res = new CasAuthenticationEntryPoint();
        res.setLoginUrl(casLoginUrl);
        res.setServiceProperties(serviceProperties());
        return res;
    }

    @Bean("serviceProperties")
    public ServiceProperties serviceProperties() {
        ServiceProperties res = new ServiceProperties();
        res.setAuthenticateAllArtifacts(true);
        res.setSendRenew(false);
        res.setService(casServiceUrl);
        return res;
    }

    @Bean("singleSignOutFilter")
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter res = new SingleSignOutFilter();
        res.setCasServerUrlPrefix(casLogoutUrl);
        res.setIgnoreInitConfiguration(true);
        return res;
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter filter = new LogoutFilter(logoutSuccessHandler(), logoutHandler());
        filter.setFilterProcessesUrl("/j_spring_security_logout");
        return filter;
    }

    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean("delegateLogoutSuccessHandler")
    public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler res = new SimpleUrlLogoutSuccessHandler();
        res.setDefaultTargetUrl(casLogoutUrl + "?service=" + casApplicationLogoutPage);
        res.setTargetUrlParameter("/j_spring_security_logout");
        return res;
    }

    @Bean("permissionsService")
    public PermissionsService permissionsService() {
        return new PermissionsService();
    }

    @Bean("liferayUserService")
    public LiferayUserService liferayUserService() {
        return new LiferayUserService();
    }
}
