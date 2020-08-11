package com.nacid.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.nacid.bl.impl.Utils;
import com.nacid.web.config.xml.Mapping;
import com.nacid.web.config.xml.Webconfig;
import com.nacid.web.exceptions.HandlerException;
import com.nacid.web.handlers.HttpRequestHandler;

public class RequestProcessor implements java.io.Serializable {
	private static Map<String, String> urlActionMapping = new HashMap<String, String>();
	private String configLocation; 
	public RequestProcessor(String configLocation) {
	    this.configLocation = configLocation;
	}
	public void init() {
    parseConfig();
  }
	
	/**
   * This method is the core of the RequestProcessor. It receives all requests
   * and sets result in the session. For a particular request takes path info, e.g.
   * /search/character, finds corresponding request handler and invokes its
   * processRequest() method. Result from processing should be stored in session.
   * If there is no result in session, content of FileNotFound.html is returned
   * as a result.
   *
   * @param request HttpRequest passed to the mainservlet's get method.
   * @throws Exception
   * @param response
   */
	public void processRequest(HttpServletRequest request, HttpServletResponse response)/* throws Exception*/ {

    String pathInfo = request.getPathInfo();
    String requestHandlerClass = getRequestHandlerClass(pathInfo);
    if (requestHandlerClass == null) {
        throw Utils.logException(new HandlerException("No handler defined for given url: " + pathInfo));
    }
    HttpRequestHandler httpRequestHandler = getRequestHandler(requestHandlerClass, request);
    if (httpRequestHandler != null) {
    	httpRequestHandler.initHandler(request, response);
    	httpRequestHandler.processRequest(request, response);
    }
  }
  private String getRequestHandlerClass(String pathInfo) {
    if (urlActionMapping.get(pathInfo) == null) {
      int lastSlashPos = pathInfo.lastIndexOf("/");
      if (lastSlashPos > 0) {
        pathInfo = pathInfo.substring(0, lastSlashPos);
        return getRequestHandlerClass(pathInfo);
      } else {
        return null;
      }
    }

    return urlActionMapping.get(pathInfo);
  }

  /**
   * Creates an instance of handler's class with name className
   *
   * @param className fullyqualified class name of a handler
   * @return an instance of handler, specified by className
   */

	private HttpRequestHandler getRequestHandler(String className, HttpServletRequest request) {
		HttpRequestHandler handler = null;
    try {
      handler = (HttpRequestHandler) Class.forName(className).getConstructor(ServletContext.class).newInstance(request.getSession().getServletContext());

//      handler = (HttpRequestHandler) Class.forName(className).newInstance();
      
    } catch (NullPointerException npe) {
      npe.printStackTrace();
    } catch (Exception e) {
    	System.out.println("RequestProcessor caught loading handler: ");
    	e.printStackTrace();
    }
    return handler;
	}
	private void parseConfig() {
		
		try {
			JAXBContext jc = JAXBContext.newInstance("com.nacid.web.config.xml");
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      Webconfig webconfig = (Webconfig) unmarshaller.unmarshal(RequestProcessor.class.getClassLoader().getResourceAsStream(
      		configLocation));
			
			for (Mapping mapping:webconfig.getRequest2Handler().getMapping()) {
				urlActionMapping.put(mapping.getUrl(), mapping.getHandler());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Map<String, String> getUrlActionMapping() {
		return urlActionMapping;
	}
	
	public static void main(String args[]) {
		RequestProcessor requestProcessor = new RequestProcessor("com/nacid/web/config/xml/webconfig.xml");
		requestProcessor.parseConfig();
	}
}
