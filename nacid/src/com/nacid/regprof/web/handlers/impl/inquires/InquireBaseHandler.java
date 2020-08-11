package com.nacid.regprof.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegProfApplicationForInquireImpl;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.regprof.web.model.inquires.ScreenFormatWebModel;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.table.TableWebModel;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author ggeorgiev
 * Vlichki klasove, koito nasledqvat InquireBaseHandler, trqbva da izvikat handlePrint() na InquireBaseHandler, sled koeto trqbva da izvikat 
 * request.setAttribute(WebKeys.NEXT_SCREEN, ...);
 */
public abstract class InquireBaseHandler extends RegProfBaseRequestHandler {
	
	private static final int PRINT_TYPE_XLS = 1;
	private static final int PRINT_TYPE_INQUIRES_SCREENS = 2;
	public InquireBaseHandler(ServletContext servletContext) {
		super(servletContext);

	}
	
	public InquireBaseHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
		super(nacidDataProvider, groupId, servletContext);
	}

	public final void handleList(HttpServletRequest request, HttpServletResponse response) {
		//System.out.println("Inside Inquire Base Handler's handle list");
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(getTableName());
		//System.out.println("table = " + table + " onlyActive?" + onlyActive);
		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		if (reloadTable || table == null) {
			table = constructEmptyTable(request);
			session.setAttribute(getTableName(), table);
			resetTableData(request, getApplications(request));
		}

		// TableState settings
		TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);


		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel(null);

		webmodel.setGroupName("regprofapplication");
		webmodel.setFormGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
		
		webmodel.setViewOpenInNewWindow(true);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		
		
		inquireCustomHandleList(request, response);
		
		request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");
		
	}
	

	public final void handlePrint(HttpServletRequest request, HttpServletResponse response) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		
		int printType = DataConverter.parseInt(request.getParameter("print_type"), 1);
		InputStream inputStream;
		if (printType == PRINT_TYPE_XLS) {
			List<ScreenFormatWebModel> webmodel = getScreenFormatWebModel(request, response);
            inputStream = TemplateGenerator.generateRegprofApplicationsXlsReport(nacidDataProvider, webmodel);
			response.setHeader("content-disposition", "attachment; filename=print.xls");
			response.setContentType("application/vnd.ms-excel");
		} else if (printType == PRINT_TYPE_INQUIRES_SCREENS) {
			Map<String, Boolean> elements = getScreenMap(request, response);
			List<ScreenFormatWebModel> webmodel = getScreenFormatWebModel(request, response);
			inputStream = TemplateGenerator.generateRegprofInquiresScreensReport(nacidDataProvider, elements, webmodel);
			response.setHeader("content-disposition", "attachment; filename=screen.doc");
			response.setContentType("application/msword");
		} else {
			throw new RuntimeException("Unknown print type...");
		}
		
		try {
			ServletOutputStream sos = response.getOutputStream();

			int read = 0;
			byte[] buf = new byte[1024];

			while ((read = inputStream.read(buf)) > 0) {
				sos.write(buf, 0, read);
			}

		} catch (Exception e) {
			throw Utils.logException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw Utils.logException(e);
				}
			}
		}
	}
	private Map<String, Boolean> getScreenMap(HttpServletRequest request, HttpServletResponse response) {
		Integer screen = DataConverter.parseInteger(request.getParameter("screen"), null);
		/**
		 * popylva map-a samo s elementite, koito shte trqbva da se izbejdat na ekrana
		 */
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (screen != null) {
			switch (screen) {
			case 1:
				map = ScreenFormatWebModel.screen1Elements;
				break;
			case 2:
				map = ScreenFormatWebModel.screen2Elements;
				break;
			case 3:
				map = ScreenFormatWebModel.screen3Elements;
				break;
			case 4:
				map = ScreenFormatWebModel.screen4Elements;
				break;
			case 5:
				String[] screenOptions = DataConverter.parseString(request.getParameter("screen_options"), "").split(";");
				if (screenOptions != null && screenOptions.length > 0) {
					for (String s:screenOptions) {
						map.put(s, true);
					}
				}
				break;
			default:
				throw new RuntimeException("no screen defined for id=" + request.getParameter("screen") );
			}
		} else {
			throw new RuntimeException("no screen defined for id=" + request.getParameter("screen") );
		}
		return map;
	}
	private List<ScreenFormatWebModel> getScreenFormatWebModel(HttpServletRequest request, HttpServletResponse response) {
		List<RegProfApplicationForInquireImpl> apps = getApplications(request);
		List<ScreenFormatWebModel> webmodel = null;
		if (apps != null) {
			webmodel = new ArrayList<ScreenFormatWebModel>();
			for (RegProfApplicationForInquireImpl a:apps) {
				webmodel.add(new ScreenFormatWebModel(a));
			}
		}
		return webmodel;
	}
	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("elements", getScreenMap(request, response));
		List<ScreenFormatWebModel> webmodel = getScreenFormatWebModel(request, response);
		if (webmodel != null) {
			request.setAttribute("screenWebModels", webmodel);	
		} else {
			request.setAttribute("emptyData", "Няма данни с избраните критерии за търсене!");
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, "inquires_screens");
	}
	
	protected abstract Table constructEmptyTable(HttpServletRequest request);
	protected abstract void resetTableData(HttpServletRequest request, List<RegProfApplicationForInquireImpl> apps);
	
	/**
	 * method, koito vry6ta Application objects koito trqbva da se izvedqt v tablicata na specifi4niq Handler, koito nasledqva InquireBaseHandler, v zavisimost ot konkretnite mu filtri
	 * 
	 * @param request
	 * @return
	 */
	protected abstract List<RegProfApplicationForInquireImpl> getApplications(HttpServletRequest request);
	/**
	 * TODO
	 * @param request
	 * @return {@link ApplicationDetailsForReport} obejcts, koito trqbva da se printirat ot konkretniq handler v zavisimost ot izbranite filtri v nandler-a
	 */
	//protected abstract List<ApplicationDetailsForReport> getApplicationDetailsForReport(HttpServletRequest request);
	/**
	 * @return imeto na atributa na tablicata, koito se slaga v sesiqta!
	 */
	protected abstract String getTableName();
	
	/**
	 * tozi method se predifinira ot klasovete, koito trqbva da pravqt custom nastrojki pri handleList... 
	 * @param request
	 * @param response
	 */
	protected void inquireCustomHandleList(HttpServletRequest request, HttpServletResponse response) {
	    
	}

}
