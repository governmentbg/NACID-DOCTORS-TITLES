package com.nacid.web.handlers;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.users.User;
import com.nacid.web.HandlerToGroupManager;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.HandlerException;
import com.nacid.web.model.common.SystemMessageWebModel;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequestHandler implements HttpRequestHandler {
  private NacidDataProvider nacidDataProvider;
  private Integer groupId;
  private ServletContext servletContext;
  protected static Logger logger = Logger.getLogger(BaseRequestHandler.class);
  public BaseRequestHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
	  this.servletContext = servletContext;
	  this.groupId = groupId;
	  this.nacidDataProvider = nacidDataProvider;
  }
  public BaseRequestHandler(ServletContext servletContext) {
    this.nacidDataProvider = (NacidDataProvider) servletContext.getAttribute(WebKeys.NACID_DATA_PROVIDER);
    this.groupId = ((HandlerToGroupManager) servletContext.getAttribute(WebKeys.HANDLER_TO_GROUP)).getGroupId(this.getClass().getName());
    this.servletContext = servletContext;
  }
  
  protected ServletContext getServletContext() {
      return servletContext;
  }
  
  public void addSystemMessageToSession(HttpServletRequest request, String msgName, SystemMessageWebModel msg) {
      HttpSession session = request.getSession();
      Map<String, SystemMessageWebModel> messageMap = 
          (Map<String, SystemMessageWebModel>)session.getAttribute(SYSTEM_MESSAGES_MAP);
      if(messageMap == null) {
          messageMap = new HashMap<String, SystemMessageWebModel>();
          session.setAttribute(SYSTEM_MESSAGES_MAP, messageMap);
      }
      messageMap.put(msgName, msg);
  }
  
  public void processRequest(HttpServletRequest request, HttpServletResponse response) {
    User user = getLoggedUser(request, response);
    String operationName = getOperationName(request);
    int operationId = UserOperationsUtils.getOperationId(operationName);
    request.setAttribute(WebKeys.GROUP_ID, getGroupId());
    int webApplicationId = (Integer) request.getSession().getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID);
    
      if (user.hasAccess(getGroupId(), operationId, webApplicationId)) { 
        if (operationId == UserOperationsUtils.OPERATION_LEVEL_NEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Добавяне на");
          handleNew(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_EDIT) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
          handleEdit(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_VIEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Преглед на");
          handleView(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_SAVE) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
          handleSave(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_LIST) {
          handleList(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_DELETE) {
          handleDelete(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_PRINT) {
          handlePrint(request, response);
        } else {
          handleDefault(request, response);
        }
      } else { // User has no access to view the page!
        throw new NoAccessException();
          
      }
    
  }

  public static void writeToResponse(HttpServletResponse response, String textToWrite) {
    try {
      int length = textToWrite.length();
      char[] chars = new char[length];
      textToWrite.getChars(0, length, chars, 0);

      response.setContentType("text/plain; charset=utf-8");
      PrintWriter writer = response.getWriter();
      response.setHeader("Content-Language", "bg");

      for (int i = 0; i < length; i++) {
        writer.append(chars[i]);
      }
      writer.flush();
    } catch (Exception e) {
      //e.printStackTrace();
      throw Utils.logException(e);
    }
  }
  public static void writeInputStreamToResponse(HttpServletResponse response, InputStream is) {
	  try {
          ServletOutputStream sos = response.getOutputStream();

          int read = 0;
          byte[] buf = new byte[1024];

          while ((read = is.read(buf)) > 0) {
              sos.write(buf, 0, read);
          }
          sos.flush();

      } catch (Exception e) {
          throw Utils.logException(e);
      } finally {
          if (is != null) {
              try {
                  is.close();
              } catch (IOException e) {
                  throw Utils.logException(e);
              }
          }
      }
  }

  public static void logRequestParams(ServletRequest request) {
    String params = "";
    Enumeration<?> namesEnum = request.getParameterNames();
    while (namesEnum.hasMoreElements()) {
      String name = (String) namesEnum.nextElement();
      params += "\n";
      params += name;
      params += "=";
      params += request.getParameter(name);

    }
    logger.debug("Request params: " + params);

  }
  
  public static void logSessionAttributes(HttpSession session) {
    String params = "";
    Enumeration<?> namesEnum = session.getAttributeNames();
    while (namesEnum.hasMoreElements()) {
      String name = (String) namesEnum.nextElement();
      params += "\n";
      params += name;
      params += "=";
      params += session.getAttribute(name);

    }
    logger.debug("Request params: " + params);
  }
  public static void logRequestAttributes(ServletRequest request) {
    String params = "";
    Enumeration<?> namesEnum = request.getAttributeNames();
    while (namesEnum.hasMoreElements()) {
      String name = (String) namesEnum.nextElement();
      params += "\n";
      params += name;
      params += "=";
      params += request.getAttribute(name);

    }
    logger.debug("Request attributes: " + params);
  }
  public static void logRequestHeaders(HttpServletRequest request) {
      String params = "";
      Enumeration<?> namesEnum = request.getHeaderNames();
      while (namesEnum.hasMoreElements()) {
        String name = (String) namesEnum.nextElement();
        params += "\n";
        params += name;
        params += "=";
        params += request.getHeader(name);

      }
      logger.debug("Request headers: " + params);
    }
  public static String getPathInfoElement(String pathInfo, int elementIndex) {
    if (pathInfo == null) {
      return null;
    }
    String[] pathInfoElements = pathInfo.split("/");
    if (pathInfoElements.length < elementIndex + 1) {
      return null;
    }
    return pathInfoElements[elementIndex];
  }
  
  
  public void handleNew(HttpServletRequest request, HttpServletResponse response) {
    throw new HandlerException("Handler: handleNew: Method not yet written.");
  }
  public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
    throw new HandlerException("Handler: handleEdit: Method not yet written.");
  }

  public void handleList(HttpServletRequest request, HttpServletResponse response) {
    throw new HandlerException("Handler: handleEdit: Method not yet written.");
  }
  
  public void handleSave(HttpServletRequest request, HttpServletResponse response){
    throw new HandlerException("Handler: handleSave: Method not yet written.");
  }
  public void handleDelete(HttpServletRequest request, HttpServletResponse response){
    throw new HandlerException("Handler: handleDelete: Method not yet written.");
  }
  public void handlePrint(HttpServletRequest request, HttpServletResponse response){
	    throw new HandlerException("Handler: handlePrint: Method not yet written.");
	  }
  public void handleView(HttpServletRequest request, HttpServletResponse response){
      request.setAttribute(WebKeys.OPERATION_VIEW, true);
      handleEdit(request, response);
  }
  
  public static String getGroupName(HttpServletRequest request) {
    // path info: /nacid/control/users = String[]{"","users"}
    return getPathInfoElement(request.getPathInfo(), 1);
  }
  public static String getOperationName(HttpServletRequest request) {
    // path info: /nacid/control/users/edit = String[]{"","users","edit"}
    return getPathInfoElement(request.getPathInfo(), 2);
  }
  
  public final int getGroupId() {
    if (groupId == null) {
      throw new RuntimeException("No groupId is set to handler. If you want to use groupIds for this handler you have to define it in webconfig.xml");  
    } else {
      return groupId;
    }
  }
  public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
	  throw new RuntimeException("Операцията за " + request.getPathInfo() + " не е дефинирана ");
  }
  
  public final void setNacidDataProvider(NacidDataProvider nacidDataProvider) {
    this.nacidDataProvider = nacidDataProvider;
  }
  
  protected NacidDataProvider getNacidDataProvider() {
    return nacidDataProvider;
  }
  public static NacidDataProvider getNacidDataProvider(HttpSession session) {
    return (NacidDataProvider) session.getServletContext().getAttribute(WebKeys.NACID_DATA_PROVIDER);
  }
  protected static void setNextScreen(HttpServletRequest request, String screen) {
      request.setAttribute(WebKeys.NEXT_SCREEN, screen);
  }

  final public User getLoggedUser(HttpServletRequest request, HttpServletResponse response) {
      return getLoggedNacidUser(request, response);
  }
  final public static User getLoggedNacidUser(HttpServletRequest request, HttpServletResponse response) {
      HttpSession session = request.getSession();
      User user = (User) session.getAttribute(WebKeys.LOGGED_USER);
      if (user != null) {
        return user;
      }
      throw new RuntimeException("User is not logged on...");
      /*NacidDataProvider nacidDataProvider = getNacidDataProvider(request.getSession());
      UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
      
      String userName = CookieUtils.getCookieValue(request, WebKeys.COOKIE_USER);
      String userPass = CookieUtils.getCookieValue(request, WebKeys.COOKIE_PASS);
      user = usersDataProvider.loginUserByPass(userName, userPass, true, (Integer) request.getSession().getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID));
      if (user == null) {
        String addr = request.getRemoteAddr();
        user = usersDataProvider.loginUserByIp(addr);
      }
      if (user == null) {
        user = usersDataProvider.loginAnonymousUser();
      }
      session.setAttribute(WebKeys.LOGGED_USER, user);
      return user;*/
    }
}
