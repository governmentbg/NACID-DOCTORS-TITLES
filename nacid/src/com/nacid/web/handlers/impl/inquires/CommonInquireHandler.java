package com.nacid.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.inquires.ApplicationStatusesWebModel;
import com.nacid.web.model.inquires.CommissionInquireUniversityWebModel;
import com.nacid.web.model.inquires.CommonInquireWebModel;
import com.nacid.web.model.inquires.InquireInstitutionWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static com.nacid.web.handlers.impl.applications.ApplicationsHandler.generateApplicationTypesListColumn;

public class CommonInquireHandler extends InquireBaseHandler {
	private final static String COLUMN_NAME_ID = "id";
	private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
	private final static String COLUMN_NAME_DATE = "Дата";
	private final static String COLUMN_NAME_OWNER_NAME = "Имена на собственика";
	private final static String COLUMN_NAME_PERSONAL_ID = "Персонален идентификатор";
	//private final static String COLUMN_NAME_APPLICANT_CITIZENSHIP = "Гражданство";
	private final static String COLUMN_NAME_DIPLOMA_EDU_LEVEL = "ОКС по диплома";
	private final static String COLUMN_NAME_DIPLOMA_EDU_LEVEL2 = "Придобита степен";
	private final static String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
	private final static String COLUMN_NAME_RECOGNIZED_EDU_LEVEL = "Призната ОКС";
	private final static String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
	private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
	private final static String COLUMN_NAME_DOCFLOW_STATUS = "Деловоден статус";

	private final static String COLUMN_NAME_RECOGNIZED_QUALIFICATION = "Призната ПК";
	private final static String COLUMN_NAME_RECOGNIZED_PROF_GROUP = "Признато ПН";

    public CommonInquireHandler(ServletContext servletContext) {
		super(servletContext);
	}

	
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
		//super.handleView(request, response);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "countriesCombo", null, true);
		
		//ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "diplomaSpecialitiesCombo", null, true);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "diplomaQualificationsCombo", null, true);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "diplomaEducationLevelsCombo", null, true);
		ComboBoxUtils.generateProfessionGroupComboBox(null, getNacidDataProvider().getNomenclaturesDataProvider().getProfessionGroups(0, null, null), false, request, "profGroupCombo",true);
		
		//Zasega tyi kato ne se popylvat gotovi blanki, atributite v request-a za diploma i recognized speciality, qualification, educationLevel sa edni i sy6ti i prosto gi prehvyrlqm
		request.setAttribute("recognizedSpecialitiesCombo", request.getAttribute("diplomaSpecialitiesCombo"));
		request.setAttribute("recognizedQualificationsCombo", request.getAttribute("diplomaQualificationsCombo"));
		request.setAttribute("recognizedEducationLevelsCombo", request.getAttribute("diplomaEducationLevelsCombo"));
		

		//po podrazbirane ne slaga combo s legalReasons , zatova dolniq red e markiran
		//ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_LEGAL_REASON, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "legalReasonsCombo", null, true);

        OrderCriteria columnNameOrderCriteria = OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true);


		NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
		List<Integer> statusIds = CommissionInquireHandler.getAvailableStatusIds();
		List<ApplicationStatus> statuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, statusIds, null, null, false);
		ComboBoxUtils.generateNomenclaturesComboBox(null, statuses, false, request, "statusesCombo", true);

        List<ApplicationStatus> allStatuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, columnNameOrderCriteria, false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allStatuses, true, request, "allStatusesCombo", true);

        List<ApplicationStatus> allLegalStatuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, columnNameOrderCriteria, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allLegalStatuses, true, request, "allLegalStatusesCombo", true);



        List<ApplicationDocflowStatus> allDocflowStatuses = nomenclaturesDataProvider.getApplicationDocflowStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, columnNameOrderCriteria);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allDocflowStatuses, true, request, "allDocflowStatusesCombo", true);

        List<DocumentType> attachmentDocumentTypesCombo = nomenclaturesDataProvider.getDocumentTypes(null, columnNameOrderCriteria, DocCategory.APPLICATION_ATTACHMENTS);
        ComboBoxUtils.generateNomenclaturesComboBox(null, attachmentDocumentTypesCombo, false, request, "attachmentDocumentTypesCombo", true);
        


		request.setAttribute("universities", Arrays.asList(new CommissionInquireUniversityWebModel(null, new ComboBoxWebModel(null, true))));
		request.setAttribute("universities_count", 1);
		request.setAttribute("institutions", Arrays.asList(new InquireInstitutionWebModel(null, new ComboBoxWebModel(null, true))));
		request.setAttribute("institutions_count", 1);
		request.setAttribute("status_ids_count", 1);
		request.setAttribute("commission_status_ids_count", 1);
		request.setAttribute("emptyUniversityCombo", new ComboBoxWebModel(null, true));

        UsersDataProvider usersDataProvider = getNacidDataProvider().getUsersDataProvider();

        List<? extends User> users = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_ID);
        ComboBoxUtils.generateUsersComboBox(null, users, request, "usersCombo", true, true);
        ComboBoxUtils.generateUsersComboBox(null, users, request, "responsibleUsersCombo", true, true);
		generateApplicationTypeComboBox(request);

		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_DOCUMENT_RECEIVE_METHOD, nomenclaturesDataProvider, false, request, "documentReceiveMethodCombo", null, true);
		request.setAttribute(WebKeys.NEXT_SCREEN, "common_inquire");
	}
	
	protected List<ApplicationDetailsForReport> getApplicationDetailsForReport(HttpServletRequest request) {
		CommonInquireWebModel commonInquireWebModel = new CommonInquireWebModel(request, jointDegreeFlag);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationDetailsForReport> apps = nacidDataProvider.getInquiresDataProvider().getApplicationDetailsForReportForCommonInquire(commonInquireWebModel);
		return apps;
	}


	protected List<ApplicationForInquireRecord> getApplications(HttpServletRequest request) {
		CommonInquireWebModel commonInquireWebModel = new CommonInquireWebModel(request, jointDegreeFlag);
		request.setAttribute("commonInquireWebModel", commonInquireWebModel);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationForInquireRecord> apps = nacidDataProvider.getInquiresDataProvider().getApplicationsForCommonInquire(commonInquireWebModel);
		return apps;
	}


	protected String getTableName() {
		return "tableCommonInquire";
	}


	protected Table constructEmptyTable(HttpServletRequest request) {
		TableFactory tableFactory = TableFactory.getInstance();
		Table table = tableFactory.createTable();
		table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
		table.addColumnHeader(COLUMN_NAME_OWNER_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_PERSONAL_ID, CellValueDef.CELL_VALUE_TYPE_STRING);
		//table.addColumnHeader(COLUMN_NAME_APPLICANT_CITIZENSHIP, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_EDU_LEVEL2, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_QUALIFICATION, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_RECOGNIZED_PROF_GROUP, CellValueDef.CELL_VALUE_TYPE_STRING);
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
		ApplicationStatusesWebModel finalApplicationStatuses = new ApplicationStatusesWebModel();
		request.setAttribute(WebKeys.FINAL_APP_STATUSES, finalApplicationStatuses);
		//Ako se iskat samo aktivnite, togava se zarejdat vsi4ki bez izbroenite v ApplicationStatus.APPLICATIONS_ONLY_ACTIVE_STATUSES statusi, inache se vry6tat vsi4ki zapisi v bazata
		if (apps != null) {
			for (ApplicationForInquireRecord app : apps) {
				try {
					/*Person applicant = app.getApplicant();
					TrainingCourse tc = app.getTrainingCourse();
					UniversityNameAndCountry universityNameAndCountry = new UniversityNameAndCountry(tc.getUniversities(), jointDegree);
					//FlatNomenclature diplomaSpeciality = tc.getSpeciality();
					FlatNomenclature diplomaEduLevel = tc.getEducationLevel();
					FlatNomenclature recognizedEduLevel = tc.getRecognizedEducationLevel();
					List<Speciality> recognizedSpecialities = tc.getRecognizedSpecialities();
					String recognizedSpeciality = "";
					if (recognizedSpecialities != null) {
						List<String> lst = new ArrayList<String>();
						for (Speciality s:recognizedSpecialities) {
							lst.add(s.getName());
						}
						recognizedSpeciality = StringUtils.join(lst, ", ");
					}*/
					table.addRow(
							app.getId(), 
							app.getAppNumber(),
							app.getAppDate(),
							app.getOwnerNames(),
							app.getOwnerCivilId(),
							//applicant.getCitizenship() == null ? "-" : applicant.getCitizenship().getName(), 
							app.getUniversityNames(!addCountryColumn),
                            app.getUniversityCountry(),
                            app.getApplicationType() != ApplicationType.DOCTORATE_APPLICATION_TYPE ? app.getEduLevelName() : "",
							app.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE ? app.getEduLevelName() : "",
                            StringUtils.join(app.getSpecialities(), "; "),
							app.getRecognizedEduLevelName(),
                            StringUtils.join(app.getRecognizedSpecialities(), "; "),
							app.getApplicationType() != ApplicationType.DOCTORATE_APPLICATION_TYPE ? app.getRecognizedQualificationName() : "",
							app.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE ? app.getRecognizedProfGroupName() : "",
							app.getApplicationStatusName(),
							app.getDocflowStatusName(),
							generateApplicationTypesListColumn(request, app.getApplicationType(), app.getEntryNumSeries())
					);
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
