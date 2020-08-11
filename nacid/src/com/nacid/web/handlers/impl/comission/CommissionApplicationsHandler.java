package com.nacid.web.handlers.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.comission.CommissionApplicationsWebModel;
import com.nacid.web.model.comission.CommissionCalendarHeaderWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.CheckBoxFilterWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

public class CommissionApplicationsHandler extends NacidBaseRequestHandler {

	private final static String COLUMN_NAME_ID = "id";
	private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
	private final static String COLUMN_NAME_DATE = "Дата";
	private final static String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
	private final static String COLUMN_NAME_SPECIALITY = "Специалност";
	private final static String COLUMN_NAME_NAME = "Име";
	private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
	private final static String COLUMN_NAME_COUNTRY = "Държава";
	private final static String COLUMN_NAME_EXPERT_STATUS = "Статус на разглеждане";
	private final static String COLUMN_NAME_COMMISSIONS = "Други заседания, в които е разглеждано заявлението";

	private static final String FILTER_NAME_STATUS = "statusFilter";
	private static final String FILTER_APPLICATION_NUMBER = "applicationNumberFilter";
	private static final String FILTER_ONLY_ACTIVE = "onlyActiveFilter";
	
	
	/**
	 * shte pomni unikalnite stojnosti na redovete v tablicata, koito ne trqbva da se pokazvat v dolnata tablica (s vsi4ki zaqvleniq ot koito moje da se izbira) 
	 */
	private Set<Object> uncheckedIds = new HashSet<Object>();

	public CommissionApplicationsHandler(ServletContext servletContext) {
		super(servletContext);
	}
	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), -1);
		if (calendarId <= 0) {
			throw new UnknownRecordException("Unknown Commission Calendar ID:" + calendarId);
		}
		generateCommissionApplicationsWebModels( calendarId,  request,  response, getOperationName(request));
		
		CommissionCalendarHandler.generateCommissionCalendarHeaderMessage(request, getNacidDataProvider().getCommissionCalendarDataProvider().getCommissionCalendar(calendarId));
		
	}
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
			new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
			return;
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
		int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), 0);
		String strApplicationIds = request.getParameter("application_ids");
		commissionCalendarDataProvider.deleteCommissionApplications(calendarId);
		if (!"".equals(strApplicationIds)) {
	    	for(String s : strApplicationIds.split(";")) {
		        Integer id =DataConverter.parseInteger(s, null);
		        if (id != null) {
		        	commissionCalendarDataProvider.addApplicationToCalendar(calendarId, id);
		        }
	    		
		    }	
	    }
		generateCommissionApplicationsWebModels( calendarId,  request,  response,  UserOperationsUtils.getOperationName(UserOperationsUtils.OPERATION_LEVEL_EDIT));
		request.setAttribute("applicationsSaved", new SystemMessageWebModel("Данните бяха записани в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
	}
	private void generateCommissionApplicationsWebModels(int calendarId, HttpServletRequest request, HttpServletResponse response, String action){
		//Start of generating table data with selected applications for given calendar id (upper table)
		CommissionCalendarDataProvider commissionCalendarDataProvider = getNacidDataProvider().getCommissionCalendarDataProvider();
		List<ApplicationForList> applications = commissionCalendarDataProvider.getCommissionApplicationsForList(calendarId);
		if (applications != null) {
			applications.sort(Comparator.comparing(ApplicationForList::getAppDate).thenComparing(ApplicationForList::getId));
		}
		Table table = createEmptyTable();
		resetTableData(table, applications, true, calendarId);
		TableWebModel tableWebModel = new TableWebModel(null);
		tableWebModel.insertTableData(table, null);
		tableWebModel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
		tableWebModel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
		tableWebModel.hideOperation(TableWebModel.OPERATION_NAME_VIEW);
		request.setAttribute("selectedApplicationsWebModel", tableWebModel);
		//End of generating table data with selected applications for given calendar id
		
		
		request.setAttribute(WebKeys.COMMISSION_APPLICATIONS_WEB_MODEL, new CommissionApplicationsWebModel(calendarId, commissionCalendarDataProvider.getCommissionApplicationIds(calendarId)));
		
		request.setAttribute(WebKeys.COMMISSION_CALENDAR_HEADER_WEB_MODEL, new CommissionCalendarHeaderWebModel(calendarId, action, CommissionCalendarHeaderWebModel.ACTIVE_FORM_CALENDAR_APPLICATIONS_EDIT));
		
		handleList(request, response);
		/**
		 * Tyi kato handleList() slaga za next_screen commission_applications_table, za da moje kato se izvika s AJAX commission_applications/list
		 * da moje ajax-a da vyrne tablicata bez headera i footera. Poradi tazi pri4ina se nalaga handleList() da zaredi cqlata stranica s header/footer i druga informaciq 
		 */
		request.setAttribute(WebKeys.NEXT_SCREEN, "commission_applications");
	}
	

	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		
		//HttpSession session = request.getSession();
		Table table = null; //(Table) session.getAttribute(WebKeys.TABLE_COMMISSION_APPLICATIONS);

		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		if (reloadTable || table == null) {
			table = createEmptyTable();
			//session.setAttribute(WebKeys.TABLE_COMMISSION_APPLICATIONS, table);
			ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
			boolean onlyActive = isOnlyActive(request);

            List<ApplicationForList> applications = applicationsDataProvider.getApplicationsForList(ApplicationType.RUDI_APPLICATION_TYPE, null, onlyActive);
			resetTableData(table, applications , false, DataConverter.parseInteger(request.getParameter("calendar_id"), null));

		}

		
		TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
		TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS, request, table, tableState);
		TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_APPLICATION_NUMBER, COLUMN_NAME_APPLICATION_NUMBER, request, table, tableState);
		addUncheckedRowsIdsToTableState(request, table, tableState, uncheckedIds);

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel(null);

		webmodel.setGroupName("applications");
		webmodel.insertTableData(table, tableState);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
		webmodel.setViewOpenInNewWindow(true);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = new FiltersWebModel();
		filtersWebModel.addFiler(generateStatusFilterComboBox(request.getParameter(FILTER_NAME_STATUS), getNacidDataProvider().getNomenclaturesDataProvider(), request));
		TextFieldFilterWebModel tfFWB = new TextFieldFilterWebModel(FILTER_APPLICATION_NUMBER, COLUMN_NAME_APPLICATION_NUMBER, request.getParameter(FILTER_APPLICATION_NUMBER));
		tfFWB.setLabelOnTop(true);
		tfFWB.setElementClass("brd");
		filtersWebModel.addFiler(tfFWB);
		filtersWebModel.addFiler(generateOnlyActiveFilterWebModel(request));
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

	}
	private static Table createEmptyTable() {
		TableFactory tableFactory = TableFactory.getInstance();
		Table table = tableFactory.createTable();
		table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
		table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_EXPERT_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_COMMISSIONS, CellValueDef.CELL_VALUE_TYPE_STRING);
		return table;
	}
	private void resetTableData(Table table, List<ApplicationForList> apps, boolean setUncheckedRowsIds, Integer calendarId) {
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		Map<Integer, CommissionCalendar> commissionCalendars = new HashMap<>();
		if (apps != null) {
			for (ApplicationForList app : apps) {
				try {
					List<Integer> appCalendars = app.getCommissionSessions() == null ? new ArrayList<>() : app.getCommissionSessions();

					//maha se tekushtiq nomer
					appCalendars = appCalendars.stream().filter(r -> !Objects.equals(r, calendarId)).collect(Collectors.toList());
					//ako kalendara go nqma keshiran v commissionCalendars, se prochita i se keshira
					appCalendars.forEach(c -> commissionCalendars.computeIfAbsent(c, (k) -> nacidDataProvider.getCommissionCalendarDataProvider().getCommissionCalendar(c)));
					//kolonata se generira kato se join-nat vsichi sessionNumbers
					String calendar = appCalendars.stream().map(c -> commissionCalendars.get(c).getSessionNumber() + "").collect(Collectors.joining("\n"));
					table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getAptName(), app.getUniName(), app.getUniCountryName(), app.getSpecialityName(), app.getApnStatusName(),
							getFinishedByExpertsText(app), calendar);
					if (setUncheckedRowsIds) {
						//pri zarejdane na gornata tablica, slaga ID-tata na zapisite v promenlivata uncheckedIds
						//celta e pri zarejdane na dolnata tablica zapisite s tezi ID-ta da ne figurirat tam
						uncheckedIds.add(app.getId());
					}
						
				} catch (IllegalArgumentException e) {
					throw Utils.logException(e);
				} catch (CellCreationException e) {
					throw Utils.logException(e);
				}
			}
		}
	}

	private static ComboBoxFilterWebModel generateStatusFilterComboBox(String activeCountryName, NomenclaturesDataProvider nomenclaturesDataProvider,
			HttpServletRequest request) {
		ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryName, true);

		List<ApplicationStatus> statuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(
                OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);
		if (statuses != null) {
			for (FlatNomenclature s : statuses) {
				combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
			}
		}
		ComboBoxFilterWebModel result = new ComboBoxFilterWebModel(combobox, FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS);
		result.setLabelOnTop(true);
		result.setElementClass("brd");
		return result;
	}
	
	private static CheckBoxFilterWebModel generateOnlyActiveFilterWebModel(HttpServletRequest request) {
		CheckBoxFilterWebModel result = new CheckBoxFilterWebModel(FILTER_ONLY_ACTIVE, 
				"Само в процедура",
				isOnlyActive(request)
				);
		result.setLabelOnTop(true);
		result.setElementClass("flt_rgt");
		return result;
	}
	
	//Prevry6ta uniqueColumnIds kym rowIds. Tyi kato tableState-a razbira edinstveno ot vytre6niq nomer na redovete, 
	//a gornata tablica (s ve4e selectnatite zaqvleniq(chlenove na komisiqta) razbira samo ot stoinostta v unikalnata kolona 
	static void addUncheckedRowsIdsToTableState(HttpServletRequest request, Table table, TableState state, Set<Object> uncheckedIds) {
		Set<String> uniqueRowsIds = RequestParametersUtils.getParameterUnCheckedRows(request, null);
		
		if (uniqueRowsIds != null) {
			for (String s : uniqueRowsIds) {
				Integer val = DataConverter.parseInteger(s, null);
				if (val != null) {
					uncheckedIds.add(val);	
				}
			}
		}
		try {
			Set<Integer> x = table.getRowIdsByUniqueColumnValues(uncheckedIds);
			state.setUncheckedRowsIds(x);  
		} catch (CellCreationException e) {
			throw Utils.logException(e);
		}

	}
    public static String getFinishedByExpertsText(ApplicationForList app) {
        return app.getExpertsCount() == 0 ? "" : (app.getExpertsProcessedStatus() != 0  ? "Експертите са приключили работата по заявлението" : "Експертите още работят по заявлението");
    }

	private static boolean isOnlyActive(HttpServletRequest request) {
		//Po default (nqma submitnata forma (v slu4aq forma se submitva edinstveno pri izbirane na filtri))
		//ili ako e submitnata formata za editvane na commission_applications, se vry6tat samo aktivnite zapisi
    	return DataConverter.parseBoolean(request.getParameter("commission_apps_edit")) || !RequestParametersUtils.getParameterFormSubmitted(request) ? true : DataConverter.parseBoolean(request.getParameter(FILTER_ONLY_ACTIVE));
	}
	public static ComboBoxWebModel generateFinishedByExpertsCombobox(String activeElementName) {
		ComboBoxWebModel result = new ComboBoxWebModel(activeElementName, true);
		result.addItem("Експертите са приключили работата по заявлението", "Експертите са приключили работата по заявлението");
		result.addItem("Експертите още работят по заявлението", "Експертите още работят по заявлението");
		return result;
	}
}
