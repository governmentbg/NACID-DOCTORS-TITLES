package com.nacid.web.filters;

import bg.duosoft.security.nacid.service.UserDetailsImpl;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.external.ExtPersonImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by georgi.georgiev on 13.01.2015.
 */
public class PermissionFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        NacidDataProvider nacidDataProvider = (NacidDataProvider) getServletContext().getAttribute("nacidDataProvider");
        UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        //user = usersDataProvider.loginUserByPass("test", "test", false, (Integer) getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID));
        int webApplicationId = (Integer) getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID);
        if (auth != null && auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser")) { //ako nqma lognat potrebitel, to logva anonymousUser!
            user = usersDataProvider.loginAnonymousUser();
        } else if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

            user = (User) request.getSession().getAttribute(WebKeys.LOGGED_USER);
            if (user == null || !userDetails.getUsername().equals(user.getUserName())) {
                NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
                Country birthCountry = nomenclaturesDataProvider.getCountry(userDetails.getBirthCountryCode());
                Country citizenshipCountry = nomenclaturesDataProvider.getCountry(userDetails.getCitizenshipCountryCode());

                ExtPersonImpl p = new ExtPersonImpl(0, userDetails.getFirstName(), userDetails.getSecondName(), userDetails.getLastName(), userDetails.getPersonalNumber(), liferayPersonTypeToNacidCivilIdType(userDetails.getPersonalNumberType()), birthCountry == null ? null : birthCountry.getId(), userDetails.getBirthCity(), userDetails.getBirthDate(), citizenshipCountry == null ? null : citizenshipCountry.getId(), userDetails.getEmail(), null, 0, nacidDataProvider);

                user = usersDataProvider.loginPortalUser(
                        userDetails.getUsername(),
                        webApplicationId,
                        p, userDetails.hasRole("ROLE_BACKOFFICE_ADMINISTRATOR_USER"));
            }
        }

        if (user == null) {
            SecurityContextHolder.getContext().setAuthentication(null);
            RequestDispatcher reqDisp = request.getRequestDispatcher("/accessdenied.jsp");
            reqDisp.forward(request, servletResponse);
            return;
        } else {
            request.getSession().setAttribute(WebKeys.LOGGED_USER, user);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    private Integer liferayPersonTypeToNacidCivilIdType(String personalNumberType) {
        Integer res = null;
        if (StringUtils.isEmpty(personalNumberType)) {
            res = null;
        } else if (personalNumberType.equalsIgnoreCase("ЕГН")) {
            res = 1;
        } else if (personalNumberType.equalsIgnoreCase("ЛНЧ")) {
            res = 2;
        } else if (personalNumberType.toLowerCase().contains("Лице без Идентификатор".toLowerCase())) {
            res = 3;
        }
        return res;

    }
}
