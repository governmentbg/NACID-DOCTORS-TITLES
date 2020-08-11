package com.nacid.web;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.db.applications.AttachmentDB;
import com.nacid.db.utils.DatabaseUtils;
import com.nacid.timers.ReceiveMailsTask;
import com.nacid.timers.ResendWaitingMailsTask;
import com.nacid.timers.TimerFactory;
import com.nacid.web.handlers.HttpRequestHandler;
import com.nacid.web.model.common.SystemMessageWebModel;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.nacid.bl.utils.UtilsDataProvider.ATTACHMENTS_BASE_DIR;

/**
 * Entry point for all browser requests contain "action".
 * Depending on the URL different actions are taken.
 * Request is passed to RequestProcessor for processing.
 * The result of processing is stored in session.
 * Finally getServletConfig().getServletContext().getRequestDispatcher("/screens/newRequestForm.jsp").forward(request, response)
 * is executed in order to forward the request to the proper view (.JSP template)
 *
 */
public class MainServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(MainServlet.class);
    private boolean isAjax;
	private TimerFactory timerFactory = TimerFactory.getTimerFactor();
	private String configLocation;
	private static Map<String, Integer> webAppNameToWebAppId = new HashMap<String, Integer>();
	static {
	    webAppNameToWebAppId.put("/nacid", 1);
	    webAppNameToWebAppId.put("/nacid_ext", 2);
	    webAppNameToWebAppId.put("/nacid_regprof", 3);
	    webAppNameToWebAppId.put("/nacid_regprof_ext", 4);
	    // 4
	}
	static {
		//zarejda pluginnite za image encode
		//IIORegistry.getDefaultInstance().registerApplicationClasspathSpis();
		ImageIO.scanForPlugins();
	}

	public void init() {
		configLocation = getServletConfig().getInitParameter("configLocation");
	    generatePropertiesBundle();
		logger.debug("Initializing application's Nacid Main Servlet...."  + getServletContext().getInitParameter("pathPrefix"));
		
		getRequestProcessor();
		getScreenManager();
		getHandler2GroupManager();
		NacidDataProvider nDP = loadNacidDataProvider();
		
		//initializing nomenclaturesDataProvider...
		nDP.getNomenclaturesDataProvider();
		AttachmentDB.FILE_LOCATION_BASE = nDP.getUtilsDataProvider().getCommonVariableValue(ATTACHMENTS_BASE_DIR);
		
		getServletContext().setAttribute("pathPrefix", getServletContext().getInitParameter("pathPrefix"));
		getServletContext().setAttribute("messages", MessagesBundle.getMessagesBundle());
		getServletContext().setAttribute("validations", ValidationStrings.getValidationStrings());
		getServletContext().setAttribute("webApplicationId", webAppNameToWebAppId.get(getServletContext().getAttribute("pathPrefix")));
		isAjax = Boolean.parseBoolean(getServletConfig().getInitParameter("ajaxServlet"));
		
		if (!isAjax && PropertiesBundle.isEmailsEnabled()) {
			logger.debug("Starting timers...");
			timerFactory.addTaskToTimer("resendWaitingMails", null, 20 * 60, new ResendWaitingMailsTask());
			timerFactory.addTaskToTimer("receiveWaitingMails", null, 5 * 60, new ReceiveMailsTask(nDP));
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws
	IOException, ServletException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		try {
			request.setAttribute("ajaxServlet", isAjax);

			request.setCharacterEncoding("utf-8");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-store, no-cache, post-check=0, pre-check=0, must-revalidate");
			response.setHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");

			HttpSession session = request.getSession();

			Map<String, SystemMessageWebModel> msgMap = (Map<String, SystemMessageWebModel>)session.getAttribute(HttpRequestHandler.SYSTEM_MESSAGES_MAP);
			if(msgMap != null) {
                for(String key : msgMap.keySet()) {
                    request.setAttribute(key, msgMap.get(key));
                }
                session.removeAttribute(HttpRequestHandler.SYSTEM_MESSAGES_MAP);
            }

			String pathInfo = request.getPathInfo();
			//try {
			//synchronized (session) {
			if (pathInfo != null) {
                logger.debug("pathInfo:" + pathInfo);
                getRequestProcessor().processRequest(request, response);
            }
			//}
		/*} catch (Exception e) {
      e.printStackTrace();
    }*/

			String nextScreenName = (String) request.getAttribute(WebKeys.NEXT_SCREEN);
			if (nextScreenName != null) {
                String nextScreen = getScreenManager().getJspTemplate(nextScreenName, false);
                logger.debug("nextScreen: " + nextScreen);
                if ( nextScreen != null ) {
                    RequestDispatcher reqDisp = getServletContext().getRequestDispatcher(nextScreen);
                    reqDisp.forward(request, response);
                } else {
                    throw new RuntimeException("No operation defined for screenName : " + nextScreenName);
                }
            } else {
                //TODO: kakvo shte se pravi ako nqma settnat nikakyv next screen??? (po default ajax request-ovete nqma da imat settnat takyv!!!!)
            }
		} catch (ServletException | IOException | RuntimeException e) {
			Utils.logException(e);
			throw e;
		}

	}

	protected HandlerToGroupManager getHandler2GroupManager() {
		HandlerToGroupManager handlerToGroupManager = (HandlerToGroupManager)
		getServletContext().getAttribute(WebKeys.HANDLER_TO_GROUP);

		if (handlerToGroupManager == null) {
			handlerToGroupManager = new HandlerToGroupManager(configLocation);
			handlerToGroupManager.init();
			getServletContext().setAttribute(WebKeys.HANDLER_TO_GROUP, handlerToGroupManager);

		}
		return handlerToGroupManager;
	}

	protected RequestProcessor getRequestProcessor() {
		RequestProcessor requestProcessor = (RequestProcessor)
		getServletContext().getAttribute(WebKeys.REQUEST_PROCESSOR);

		if (requestProcessor == null) {
			requestProcessor = new RequestProcessor(configLocation);
			requestProcessor.init();
			getServletContext().setAttribute(WebKeys.REQUEST_PROCESSOR, requestProcessor);

		}
		return requestProcessor;
	}

	protected ScreenManager getScreenManager() {
		ScreenManager screenManager = (ScreenManager)
		getServletContext().getAttribute(WebKeys.SCREEN_MANAGER);

		if (screenManager == null) {
			screenManager = new ScreenManager(configLocation);
			screenManager.init();
			getServletContext().setAttribute(WebKeys.SCREEN_MANAGER, screenManager);
		}
		return screenManager;
	}
	protected void generatePropertiesBundle() {
		try(InputStream is = getServletContext().getResourceAsStream("/WEB-INF/classes/config.properties")) {
			PropertiesBundle propertiesBundle = (PropertiesBundle) getServletContext().getAttribute("propertiesBundle");
			synchronized (getServletContext()) {
				if (propertiesBundle == null) {
					try {
						propertiesBundle = new PropertiesBundle(is);
						getServletContext().setAttribute("propertiesBundle", propertiesBundle);
					} catch (IOException e) {
						throw Utils.logException(e);
					}
				}
			}
		} catch (IOException e) {
			throw Utils.logException(e);
		}

	}


	/**
	 * Ako site-a ne se access-va dylgo vreme (v prodyljenie na 4asove), 
	 * togava garbage collector-a moje da zabyrshe NacidDataProvider-a.
	 * Poradi tazi pri4ina go slagam kato attribute v servletContext-a - da e garantirano 4e dokato priljenieto e aktivno
	 * dataprovider-a shte e vinagi edin i sy6t!
	 * @return
	 */
	protected NacidDataProvider loadNacidDataProvider() {
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(DatabaseUtils.getDataSource());
		getServletContext().setAttribute(WebKeys.NACID_DATA_PROVIDER, nacidDataProvider);
		return nacidDataProvider;
	}

	public void destroy() {
		logger.debug("destroying application's MainServlet..." + getServletContext().getInitParameter("pathPrefix"));
		if (!isAjax && PropertiesBundle.isEmailsEnabled()) {
			logger.debug("Killing timers...");
			timerFactory.killTimers();
		}

	}
}
