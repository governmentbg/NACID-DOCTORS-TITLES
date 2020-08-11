package com.ext.nacid.web.handlers.impl.inquires;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.applications.University;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.inquires.ScreenFormatWebModel;
import com.nacid.web.model.table.TableWebModel;
import org.apache.commons.lang.StringUtils;

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
public abstract class InquireBaseHandler extends NacidExtBaseRequestHandler {
	/**
	 * shte pokazva dali tablicite ot handleList(), 
	 * koito trqbva da se generirat sa za joint degrees (pri joint degrees kolonite University name i University country sa obedineni)
	 * Stojnosti
	 * - null - nqma znachenie (t.e. moje i da ima joint degrees, moje i da nqma)
	 * - 1 - samo joint degrees
	 * - 0 - bez joint degrees
	 */
	protected Integer jointDegreeFlag;
	protected boolean addCountryColumn;;
	protected final static String COLUMN_NAME_UNIVERSITY_NAME = MessagesBundle.getMessagesBundle().getValue("University");
	protected final static String COLUMN_NAME_UNIVERSITY_COUNTRY = "Държава";
	
	private static final int PRINT_TYPE_XLS = 1;
	private static final int PRINT_TYPE_INQUIRES_SCREENS = 2;
	public InquireBaseHandler(ServletContext servletContext) {
		super(servletContext);

	}
	
	/*public void handleView(HttpServletRequest request, HttpServletResponse response) {
		User u = getLoggedUser(request, response);
		request.setAttribute("show_filter", u.hasAccess(getGroupId(), UserAccessUtils.getOperationId("list")));
		request.setAttribute("show_print", u.hasAccess(getGroupId(), UserAccessUtils.getOperationId("print")));
	}*/

	public final void handleList(HttpServletRequest request, HttpServletResponse response) {
		//System.out.println("Inside Inquire Base Handler's handle list");
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(getTableName());
		if (DataConverter.parseBoolean(request.getParameter("joint_degree"))) {
			jointDegreeFlag = null;//nqma znachenie (moje i da ima, moje i da nqma)
			addCountryColumn = false;
		} else if (DataConverter.parseBoolean(request.getParameter("only_joint_degree"))) {
			jointDegreeFlag = 1;
			addCountryColumn = false;
		} else {
			jointDegreeFlag = 0;//bez joint degrees
			addCountryColumn = true;
		}
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

		webmodel.setGroupName("expertreport");
		webmodel.setFormGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
		//webmodel.addRequestParam(TableWebModel.OPERATION_NAME_VIEW, WebKeys.APPLICATION_BACK_URL, getGroupName(request) + "/view");
		webmodel.setViewOpenInNewWindow(true);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		//Ako shte se pokazvat jointDegrees i sy6testvuva kolona COLUMN_NAME_UNIVERSITY_COUNTRY, togava tq se skriva, zashtoto za jointDegrees informaciqta za universityCountry se pazi v kolonata universityName  
		if (!addCountryColumn && webmodel.getColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY) != null) {
			webmodel.hideUnhideColumn(COLUMN_NAME_UNIVERSITY_COUNTRY, true);
		}

		request.setAttribute(WebKeys.NEXT_SCREEN, "inquire_list");
	}
	

	public final void handlePrint(HttpServletRequest request, HttpServletResponse response) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		
		int printType = DataConverter.parseInt(request.getParameter("print_type"), 1);
		InputStream inputStream;
		if (printType == PRINT_TYPE_XLS) {
			List<ApplicationDetailsForReport> apps = getApplicationDetailsForReport(request);
			inputStream = TemplateGenerator.generateApplicationsXlsReport( nacidDataProvider,  apps);
			response.setHeader("content-disposition", "attachment; filename=print.xls");
			response.setContentType("application/vnd.ms-excel");
		} else if (printType == PRINT_TYPE_INQUIRES_SCREENS) {
			Map<String, Boolean> elements = getScreenMap(request, response);
			List<ScreenFormatWebModel> webmodel = getScreenFormatWebModel(request, response);
			inputStream = TemplateGenerator.generateInquiresScreensReport(nacidDataProvider, elements, webmodel);
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
		Map<String, Boolean> map = getScreenMap(request, response);
		List<ApplicationForInquireRecord> apps = getApplications(request);
		List<ScreenFormatWebModel> webmodel = null;
		if (apps != null) {
			webmodel = new ArrayList<ScreenFormatWebModel>();
			for (ApplicationForInquireRecord a:apps) {
				webmodel.add(new ScreenFormatWebModel(a, map, getNacidDataProvider()));
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
	protected abstract void resetTableData(HttpServletRequest request, List<ApplicationForInquireRecord> apps);
	
	/**
	 * method, koito vry6ta Application objects koito trqbva da se izvedqt v tablicata na specifi4niq Handler, koito nasledqva InquireBaseHandler, v zavisimost ot konkretnite mu filtri
	 * 
	 * @param request
	 * @return
	 */
	protected abstract List<com.nacid.data.inquire.ApplicationForInquireRecord> getApplications(HttpServletRequest request);
	/**
	 * @param request
	 * @return {@link ApplicationDetailsForReport} obejcts, koito trqbva da se printirat ot konkretniq handler v zavisimost ot izbranite filtri v nandler-a
	 */
	protected abstract List<ApplicationDetailsForReport> getApplicationDetailsForReport(HttpServletRequest request);
	/**
	 * @return imeto na atributa na tablicata, koito se slaga v sesiqta!
	 */
	protected abstract String getTableName();

}
/**
 * klas, koito generira universityName i universityCountry za tablicite.
 * @author ggeorgiev
 */
class UniversityNameAndCountry {
	private String universityName = "-";
	private String universityCountry = "-";
	/**
	 * Ako na konstruktora se podade jointDegree = true, v universityName ima "universityName, universityCountry" za vsi4ki universities, a v universityCountry nqma nishto
	 * Ako na kontruktora se podade jointDegree = false, v universityName ima "universityName" a v universityCountry - "universityCountry" za PYRVIQ universitet v spisyka (teoreti4no v tozi slu4ai se o4akva 4e za universities.size() <= 1)
	 * 
	 */
	public UniversityNameAndCountry(List<? extends University> universities, boolean jointDegree) {
		if (universities == null || universities.size() == 0) {
			return;
		}
		if (!jointDegree) { //za universityName se slaga imeto na pyrviq universitet, a za universityCountry dyrjavata na pyrviq universitet
			University u = universities.get(0);
			universityName = u.getBgName();
			universityCountry = u.getCountry() == null ? "-" : u.getCountry().getName();
		} else {
			List<String> lst = new ArrayList<String>();
			for (University u:universities) {
				Country c = u.getCountry();
				String str = u.getBgName();
				if (c != null) {
					str += ", " + c.getName();
				}
				lst.add(str);
			}
			universityName = StringUtils.join(lst, ";<br /> ");
			universityCountry = "";
		}
	}
	public String getUniversityName() {
		return universityName;
	}
	public String getUniversityCountry() {
		return universityCountry;
	}
}
