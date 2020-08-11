package com.nacid.web.filters;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
/***
 * dobavq v bazata 4e daden user s dadeno sessionId, remoteAddress i remoteHost se e lognal v sistemata!
 * @author ggeorgiev
 *
 */
/**
 * TODO vyznikva edin vypros - dali bazata nqma da se pylni s Anonymous users login/logoff/
 * pri vsqko logoffvane na daden user, se logon-va anonymous 
 * kakvo stava ako daden user iska da se logoffne kato pesho i da se logne kato ivan
 * maj pyrvo se nulira rekorda za pesho, sled tova se syzdava nov za anonymous - do momenta na validiraneto na ivan,
 * koito se "logoffva" sled nqkolko milisecundi - vremeto mejdu zarejdaneto na filter-a i userLoginHandler-a, sled koeto se logon-va ivan...
 * t.e. ima edin paraziten red anonymous, kojto teoreti4no maj ne e mnogo dobre da sy6testvuva.... 
 */
public class UserSysLogStarter extends GenericFilterBean {

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
    
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;
    HttpSession session = request.getSession();
    NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(session);
    UsersDataProvider usersSysLogDataProvider = nacidDataProvider.getUsersDataProvider();
    if (!usersSysLogDataProvider.isUserSysLoggingStarted(session.getId())) {
      User user = NacidBaseRequestHandler.getLoggedNacidUser(request, response);
      int webApplicationId = (Integer) request.getSession().getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID);
      usersSysLogDataProvider.startUserSysLogging(user.getUserId(), session.getId(), request.getRemoteAddr(), request.getRemoteHost(), webApplicationId);
    }
    filterChain.doFilter(req, resp);
  }

}
