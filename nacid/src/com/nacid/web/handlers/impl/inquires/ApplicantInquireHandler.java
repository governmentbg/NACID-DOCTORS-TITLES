package com.nacid.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;
import com.nacid.web.model.inquires.ApplicantInquireWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.nacid.web.handlers.impl.applications.ApplicationsHandler.generateApplicationTypesListColumn;

public class ApplicantInquireHandler extends InquireBaseHandler {
	private final static String COLUMN_NAME_ID = "id";
	private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
	private final static String COLUMN_NAME_DATE = "Дата";
	private final static String COLUMN_NAME_OWNER_NAME = "Имена на собственика";
	private final static String COLUMN_NAME_APPLICANT_CITIZENSHIP = "Гражданство";
	private final static String COLUMN_NAME_UNIVERSITY_NAME_AND_COUNTRY = MessagesBundle.getMessagesBundle().getValue("University");
	private final static String COLUMN_NAME_DIPLOMA_EDU_LEVEL = "ОКС по диплома";
	private final static String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
	private final static String COLUMN_NAME_RECOGNIZED_EDU_LEVEL = "Призната ОКС";
	private final static String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
	private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
	private final static String COLUMN_NAME_DOCFLOW_STATUS = "Деловоден статус";

	public ApplicantInquireHandler(ServletContext servletContext) {
		super(servletContext);
	}

	
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
	    ApplicationsHandler.generateCompaniesCombo(request, getNacidDataProvider(), null);
		generateApplicationTypeComboBox(request);

		request.setAttribute(WebKeys.NEXT_SCREEN, "applicant_inquire");
	}
	
	protected List<ApplicationDetailsForReport> getApplicationDetailsForReport(HttpServletRequest request) {
		ApplicantInquireWebModel applicantInquireWebModel = new ApplicantInquireWebModel(request, jointDegreeFlag);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationDetailsForReport> apps = nacidDataProvider.getInquiresDataProvider().getApplicationDetailsForReportForApplicantInquire(applicantInquireWebModel);
		return apps;
	}


	protected List<ApplicationForInquireRecord> getApplications(HttpServletRequest request) {
		ApplicantInquireWebModel applicantInquireWebModel = new ApplicantInquireWebModel(request, jointDegreeFlag);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationForInquireRecord> apps = nacidDataProvider.getInquiresDataProvider().getApplicationsForApplicantInquire(applicantInquireWebModel);
		return apps;
	}


	protected String getTableName() {
		return "applicantInquireTable";
	}
	
	
	protected Table constructEmptyTable(HttpServletRequest request) {
		TableFactory tableFactory = TableFactory.getInstance();
		Table table = tableFactory.createTable();
		table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
		table.addColumnHeader(COLUMN_NAME_OWNER_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APPLICANT_CITIZENSHIP, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_NAME_AND_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DOCFLOW_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
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
		if (apps != null) {
			for (ApplicationForInquireRecord app : apps) {
				try {
					table.addRow(
							app.getId(),
                            app.getAppNumber(),
                            app.getAppDate(),
                            app.getOwnerNames(),
                            app.getOwnerCitizenshipName() == null ? "-" : app.getOwnerCitizenshipName(),
                            app.getUniversityNames(!addCountryColumn),
                            app.getEduLevelName() == null ? "-" : app.getEduLevelName(),
                            app.getSpecialityNamesSeparatedBySemiColon(),
                            app.getRecognizedEduLevelName() == null ? "-" : app.getRecognizedEduLevelName(),
                            app.getRecognizedSpecialityNamesSeparatedBySemiColon(),
                            app.getApplicationStatusName(),
							app.getDocflowStatusName(),
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
