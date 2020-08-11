package com.ext.nacid.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.inquires.ApplicationTypeAndEntryNumSeries;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.inquires.ApplicationStatusesWebModel;
import com.nacid.web.model.inquires.CommissionInquireUniversityWebModel;
import com.nacid.web.model.inquires.CommissionInquireWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class CommissionInquireHandler extends InquireBaseHandler {
	private final static String COLUMN_NAME_ID = "id";
	private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
	private final static String COLUMN_NAME_DATE = "Дата";
	//private final static String COLUMN_NAME_APPLICANT_NAME = "Име";
	private final static String COLUMN_NAME_APPLICANT_CITIZENSHIP = "Гражданство";
	private final static String COLUMN_NAME_DIPLOMA_EDU_LEVEL = "ОКС по диплома";
	private final static String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
	private final static String COLUMN_NAME_RECOGNIZED_EDU_LEVEL = "Призната ОКС";
	private final static String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
	private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
	
	
	
	public CommissionInquireHandler(ServletContext servletContext) {
		super(servletContext);
	}
	
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
		//super.handleView(request, response);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "countriesCombo", null, true);
		
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "diplomaSpecialitiesCombo", null, true);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "diplomaQualificationsCombo", null, true);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "diplomaEducationLevelsCombo", null, true);
		ComboBoxUtils.generateProfessionGroupComboBox(null, getNacidDataProvider().getNomenclaturesDataProvider().getProfessionGroups(0, null, null), false, request, "profGroupCombo", true);
		
		
		//Zasega tyi kato ne se popylvat gotovi blanki, atributite v request-a za diploma i recognized speciality, qualification, educationLevel sa edni i sy6ti i prosto gi prehvyrlqm
		request.setAttribute("recognizedSpecialitiesCombo", request.getAttribute("diplomaSpecialitiesCombo"));
		request.setAttribute("recognizedQualificationsCombo", request.getAttribute("diplomaQualificationsCombo"));
		request.setAttribute("recognizedEducationLevelsCombo", request.getAttribute("diplomaEducationLevelsCombo"));
		

		//po podrazbirane ne slaga combo s legalReasons , zatova dolniq red e markiran
		//ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_LEGAL_REASON, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "legalReasonsCombo", null, true);
		
		
		NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
		List<Integer> statusIds = getAvailableStatusIds();//Arrays.asList(ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE, ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE);
		List<ApplicationStatus> statuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, statusIds, new Date(), null, false);
		ComboBoxUtils.generateNomenclaturesComboBox(null, statuses, true, request, "statusesCombo", true);
		
		List<ApplicationStatus> allStatuses = nomenclaturesDataProvider.getApplicationStatuses(NacidDataProvider.APP_NACID_ID, new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
		ComboBoxUtils.generateNomenclaturesComboBox(null, allStatuses, true, request, "allStatusesCombo", true);

        List<ApplicationStatus> legalStatuses = nomenclaturesDataProvider.getApplicationStatuses(NacidDataProvider.APP_NACID_ID, new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allStatuses, true, request, "allLegalStatusesCombo", true);

		request.setAttribute("universities", Arrays.asList(new CommissionInquireUniversityWebModel(null, new ComboBoxWebModel(null, true))));
		request.setAttribute("universities_count", 1);
		request.setAttribute("statuses_count", 1);
		request.setAttribute("emptyUniversityCombo", new ComboBoxWebModel(null, true));
		
		request.setAttribute(WebKeys.NEXT_SCREEN, "commission_inquire");
	}
	

    
	protected List<ApplicationDetailsForReport> getApplicationDetailsForReport(HttpServletRequest request) {
		CommissionInquireWebModel commissionInquireWebModel = new CommissionInquireWebModel(request, jointDegreeFlag);
		commissionInquireWebModel.setApplicationTypeEntryNumSeries(getAppTypes());
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		return nacidDataProvider.getInquiresDataProvider().getApplicationDetailsForReportForCommissionInquire(commissionInquireWebModel);
	}
	private List<ApplicationTypeAndEntryNumSeries> getAppTypes() {
		return Arrays.asList(new ApplicationTypeAndEntryNumSeries(ApplicationType.RUDI_APPLICATION_TYPE, null, ApplicationTypeAndEntryNumSeries.JOIN_TYPE_AND));
	}
	protected List<ApplicationForInquireRecord> getApplications(HttpServletRequest request) {
		CommissionInquireWebModel commissionInquireWebModel = new CommissionInquireWebModel(request, jointDegreeFlag);
		commissionInquireWebModel.setApplicationTypeEntryNumSeries(getAppTypes());
		return getNacidDataProvider().getInquiresDataProvider().getApplicationsForCommissionInquire(commissionInquireWebModel);
		
	}

	protected String getTableName() {
		return WebKeys.TABLE_INQUIRE_APPLICATIONS;
	}
	public static List<Integer> getAvailableStatusIds() {
		return  ApplicationStatus.RUDI_APPLICATION_STATUSES_FROM_COMMISSION;
	}


	protected Table constructEmptyTable(HttpServletRequest request) {
		TableFactory tableFactory = TableFactory.getInstance();
		Table table = tableFactory.createTable();
		table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
		//table.addColumnHeader(COLUMN_NAME_APPLICANT_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APPLICANT_CITIZENSHIP, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);

		return table;

	}
	protected void resetTableData(HttpServletRequest request, List<ApplicationForInquireRecord> apps) {
		Table table = (Table) request.getSession().getAttribute(getTableName());
		if (table == null) {
			return;
		}
		table.emtyTableData();
		ApplicationStatusesWebModel finalApplicationStatuses = new ApplicationStatusesWebModel();
        request.setAttribute(WebKeys.FINAL_APP_STATUSES, finalApplicationStatuses);
        
		if (apps != null) {
			for (ApplicationForInquireRecord app : apps) {
				try {
					table.addRow(
                            app.getId(),
                            app.getAppNumber(),
                            app.getAppDate(),
							//applicant.getFullName(),  
							app.getOwnerCitizenshipName() == null ? "-" : app.getOwnerCitizenshipName(),
							app.getUniversityNames(addCountryColumn),
                            app.getUniversityCountry(),
                            app.getEduLevelName() == null ? "-" : app.getEduLevelName(),
							app.getSpecialityNamesSeparatedBySemiColon(),
							app.getRecognizedEduLevelName() == null ? "-" : app.getRecognizedEduLevelName(),
							app.getRecognizedSpecialityNamesSeparatedBySemiColon(),
							app.getApplicationStatusName());
					finalApplicationStatuses.addApplicationStatus(app.getFinalStatusName());
				} catch (IllegalArgumentException e) {
					throw Utils.logException(e);
				} catch (CellCreationException e) {
					throw Utils.logException(e);
				}
			}
		}
	}
    
}
