package com.nacid.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;
import com.nacid.web.model.inquires.EmployeeInquireWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.nacid.web.handlers.impl.applications.ApplicationsHandler.generateApplicationTypesListColumn;

public class EmployeeInquireHandler extends InquireBaseHandler {
	private final static String COLUMN_NAME_ID = "id";
	private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
	private final static String COLUMN_NAME_DATE = "Дата";
	private final static String COLUMN_NAME_OWNER_NAME = "Имена на собственика";
	private final static String COLUMN_NAME_UNIVERSITY_COUNTRY = "Държава ВУ";
	private final static String COLUMN_NAME_UNIVERSITY_NAME_AND_COUNTRY = MessagesBundle.getMessagesBundle().getValue("University");
	//private final static String COLUMN_NAME_DIPLOMA_EDU_LEVEL = "ОКС по диплома";
	//private final static String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
	//private final static String COLUMN_NAME_RECOGNIZED_EDU_LEVEL = "Призната ОКС";
	//private final static String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
	private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
	private final static String COLUMN_NAME_DOCFLOW_STATUS = "Деловоден статус";
    private final static String COLUMN_NAME_USER_NAMES = "Имена на служители работили по заявлението";
    private final static String COLUMN_NAME_RESPONSIBLE_USER = "Отговорник";
	
	public EmployeeInquireHandler(ServletContext servletContext) {
		super(servletContext);
	}

	
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
	    ApplicationsHandler.generateCompaniesCombo(request, getNacidDataProvider(), null);
	    NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
        List<ApplicationStatus> allStatuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allStatuses, true, request, "allStatusesCombo", true);

        List<ApplicationStatus> allLegalStatuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allLegalStatuses, true, request, "allLegalStatusesCombo", true);

        List<ApplicationDocflowStatus> allDocflowStatuses = nomenclaturesDataProvider.getApplicationDocflowStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        ComboBoxUtils.generateNomenclaturesComboBox(null, allDocflowStatuses, true, request, "allDocflowStatusesCombo", true);

        UsersDataProvider usersDataProvider = getNacidDataProvider().getUsersDataProvider();
        List<? extends User> users = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_ID);
        ComboBoxUtils.generateUsersComboBox(null, users, request, "usersCombo", true, true);
		ComboBoxUtils.generateUsersComboBox(null, users, request, "responsibleUsersCombo", true, true);

		generateApplicationTypeComboBox(request);

		request.setAttribute("status_ids_count", 1);
        request.setAttribute(WebKeys.NEXT_SCREEN, "employee_inquire");
	}
	
	protected List<ApplicationDetailsForReport> getApplicationDetailsForReport(HttpServletRequest request) {
	    EmployeeInquireWebModel inquireWebModel = new EmployeeInquireWebModel(request);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationDetailsForReport> apps = nacidDataProvider.getInquiresDataProvider().getApplicationDetailsForReportForEmployeeInquire(inquireWebModel);
		return apps;
	}


	protected List<ApplicationForInquireRecord> getApplications(HttpServletRequest request) {
		EmployeeInquireWebModel inquireWebModel = new EmployeeInquireWebModel(request);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationForInquireRecord> apps = nacidDataProvider.getInquiresDataProvider().getApplicationsForEmployeeInquire(
                inquireWebModel
				);
		return apps;
	}


	protected String getTableName() {
		return "employeeInquireTable";
	}
	
	
	protected Table constructEmptyTable(HttpServletRequest request) {
		TableFactory tableFactory = TableFactory.getInstance();
		Table table = tableFactory.createTable();
		table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
		table.addColumnHeader(COLUMN_NAME_OWNER_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_NAME_AND_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DOCFLOW_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_USER_NAMES, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RESPONSIBLE_USER, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_ENTRY_NUMS, CellValueDef.CELL_VALUE_TYPE_STRING);
		return table;

	}
	protected void resetTableData(HttpServletRequest request, List<ApplicationForInquireRecord> apps) {
		Table table = (Table) request.getSession().getAttribute(getTableName());
		if (table == null) {
			return;
		}
		table.emtyTableData();

		//Ako se iskat samo aktivnite, togava se zarejdat vsi4ki bez izbroenite v ApplicationStatus.APPLICATIONS_ONLY_ACTIVE_STATUSES statusi, inache se vry6tat vsi4ki zapisi v bazata
		NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(request.getSession());
		List<? extends User> users = nacidDataProvider.getUsersDataProvider().getUsers(0, 0, 0, NacidDataProvider.APP_NACID_ID);
		Map<Integer, ? extends User> usersMap = DataConverter.convertListToMap(users, "userId", Integer.class);
		if (apps != null) {
			for (ApplicationForInquireRecord app : apps) {
				try {
					table.addRow(
							app.getId(), 
							app.getAppNumber(),
							app.getAppDate(),
							app.getOwnerNames(),
							app.getUniversityNames(addCountryColumn),
							app.getUniversityCountry() == null ? "" : app.getUniversityCountry(),

							app.getApplicationStatusName(),
							app.getDocflowStatusName(),
							(app.getUsersWorked() == null || app.getUsersWorked().size() == 0 ? null : StringUtils.join(app.getUsersWorked(), ", ")),
							(app.getResponsibleUserName() == null ? null : app.getResponsibleUserName()),
							generateApplicationTypesListColumn(request, app.getApplicationType(), app.getEntryNumSeries()));
				} catch (IllegalArgumentException e) {
					throw Utils.logException(e);
				} catch (CellCreationException e) {
					throw Utils.logException(e);
				}
			}
		}
	}
    
}
