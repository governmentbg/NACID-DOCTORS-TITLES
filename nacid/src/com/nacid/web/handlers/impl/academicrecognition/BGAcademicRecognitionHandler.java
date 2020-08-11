package com.nacid.web.handlers.impl.academicrecognition;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.academicrecognition.AcademicRecognitionDataProvider;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionInfo;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;
import org.apache.commons.lang.StringUtils;

public class BGAcademicRecognitionHandler extends NacidBaseRequestHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_APPLICANT = "Заявител";
	private static final String COLUMN_NAME_CITIZENSHIP = "Гражданство";
	
	private static final String COLUMN_NAME_UNIVERSITY = "Университет";
	private static final String COLUMN_NAME_UNIVERSITY_COUNTRY = "Държава на ВУ";
	private static final String COLUMN_NAME_EDUCATION_LEVEL = "Придобита ОКС";
	private static final String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
	private static final String COLUMN_NAME_DIPLOMA_NUMBER = "Номер на диплома";
	private static final String COLUMN_NAME_DIPLOMA_DATE = "Дата на диплома";
	private static final String COLUMN_NAME_PROTOCOL_NUMBER = "Решение на АС на ВУ";
	private static final String COLUMN_NAME_DENIAL_PROTOCOL_NUMBER = "Решение на АС на ВУ (отказ/ професионален бакалавър / бакалавър / магистър)";
	private static final String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
	private static final String COLUMN_NAME_IN_NUMBER = "Входящ номер";
	private static final String COLUMN_NAME_OUT_NUMBER = "Изходящ номер";
	private static final String COLUMN_NAME_BG_UNIVERSITY_ID = "Български университет, признал дипломата";


	private static final String FILTER_NAME_BG_UNIVERSITY = "bgUniversityFilter";
	private static final String FILTER_NAME_APPLICANT_NAMES = "applicantNamesFilter";
	private static final String FILTER_NAME_CITIZENSHIP = "citizenshpFilter";
	private static final String FILTER_NAME_UNIVERSITY = "universityFilter";
	private static final String FILTER_NAME_UNIVERSITY_COUNTRY = "universityCountryFilter";
	private static final String FILTER_NAME_EDUCATION_LEVEL = "educationLevelFilter";
	private static final String FILTER_NAME_IN_NUMBER = "inNumberFilter";

	
	//private static final String FILTER_NAME_COUNTRY = "countryFilter";
	private static final String nextScreen = "bgacademicrecognition";
	private static final String attributeName = "com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl";
	private static final String similarName="similar";
	private static final String recognitionStatuses = "recognitionStatuses";
	public BGAcademicRecognitionHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		//NacidDataProvider nacidDataProvider = getNacidDataProvider();
		BGAcademicRecognitionInfoImpl rec = new BGAcademicRecognitionInfoImpl();
		rec.setCreatedDate(new Date());
		request.setAttribute(attributeName, rec);
		request.setAttribute(WebKeys.NEXT_SCREEN, nextScreen);
		generateBGUniversitiesCombobox(null, getNacidDataProvider().getUniversityDataProvider(), request);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(),
				true, request, recognitionStatuses, null, false);
	}

	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		
		int id = DataConverter.parseInt(request.getParameter("id"), -1);
		if (id <= 0) {
			throw new UnknownRecordException("Unknown university ID:" + id);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, nextScreen);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();

		AcademicRecognitionDataProvider dp = nacidDataProvider.getAcademicRecognitionDataProvider();
		BGAcademicRecognitionInfoImpl record = dp.getAcademicRecognition(id);
        NomenclaturesDataProvider nomProvider = nacidDataProvider.getNomenclaturesDataProvider();
         
		record.setSimilarRecognitions(dp.getSimilarAcademicRecognitions(record.getApplicant(), record.getUniversity(), 
				record.getUniversityCountry(), record.getEducationLevel(), record.getDiplomaSpeciality(), record.getId()));
		request.setAttribute(attributeName, record);
		request.setAttribute(similarName, record.getSimilarRecognitions());
		
		request.setAttribute("recognizedUniversities", record);
		generateBGUniversitiesCombobox(record.getRecognizedUniversityId(), getNacidDataProvider().getUniversityDataProvider(), request);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, nomProvider,
				true, request, recognitionStatuses, null, false);
	}

	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION);

		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_APPLICANT, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_CITIZENSHIP, CellValueDef.CELL_VALUE_TYPE_STRING);

			table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_EDUCATION_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);

			table.addColumnHeader(COLUMN_NAME_DIPLOMA_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DIPLOMA_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DIPLOMA_DATE, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_PROTOCOL_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DENIAL_PROTOCOL_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_RECOGNIZED_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_IN_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_OUT_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_BG_UNIVERSITY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);

			session.setAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION, table);
			resetTableData(request);

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			//TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
			TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_BG_UNIVERSITY, COLUMN_NAME_BG_UNIVERSITY_ID, request, table, tableState);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_APPLICANT_NAMES, COLUMN_NAME_APPLICANT, request, table, tableState);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_CITIZENSHIP, COLUMN_NAME_CITIZENSHIP, request, table, tableState);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_UNIVERSITY_COUNTRY, COLUMN_NAME_UNIVERSITY_COUNTRY, request, table, tableState);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_UNIVERSITY, COLUMN_NAME_UNIVERSITY, request, table, tableState);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_EDUCATION_LEVEL, COLUMN_NAME_EDUCATION_LEVEL, request, table, tableState);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_IN_NUMBER, COLUMN_NAME_IN_NUMBER, request, table, tableState);
			session.setAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на " + MessagesBundle.getMessagesBundle().getValue("recognized_diplomas"));
		// webmodel.setColumnFormatter("userDate",
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "bgacademicrecognition_list");
		webmodel.hideUnhideColumn(COLUMN_NAME_BG_UNIVERSITY_ID, true);
		webmodel.hideUnhideColumn(COLUMN_NAME_CITIZENSHIP, true);
		webmodel.hideUnhideColumn(COLUMN_NAME_EDUCATION_LEVEL, true);
		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			filtersWebModel.addFiler(generateTextFieldFilter(request, FILTER_NAME_APPLICANT_NAMES, COLUMN_NAME_APPLICANT, true, "brd w200"));
			filtersWebModel.addFiler(generateTextFieldFilter(request, FILTER_NAME_CITIZENSHIP, COLUMN_NAME_CITIZENSHIP, true, "brd w200"));
			filtersWebModel.addFiler(generateTextFieldFilter(request, FILTER_NAME_UNIVERSITY, COLUMN_NAME_UNIVERSITY, true, "brd w200"));
			filtersWebModel.addFiler(generateTextFieldFilter(request, FILTER_NAME_UNIVERSITY_COUNTRY, COLUMN_NAME_UNIVERSITY_COUNTRY, true, "brd w200"));
			filtersWebModel.addFiler(generateBGUniversitiesFilterWebModel(DataConverter.parseInt(request.getParameter(FILTER_NAME_BG_UNIVERSITY), 0), getNacidDataProvider(), request));
			filtersWebModel.addFiler(generateTextFieldFilter(request, FILTER_NAME_EDUCATION_LEVEL, COLUMN_NAME_EDUCATION_LEVEL, true, "brd w200"));
			filtersWebModel.addFiler(generateTextFieldFilter(request, FILTER_NAME_IN_NUMBER, COLUMN_NAME_IN_NUMBER, true, "brd w200"));
			//filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
		    session.setAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION
					+ WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}

	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
	    BGAcademicRecognitionInfoImpl record = (BGAcademicRecognitionInfoImpl) request.getAttribute(attributeName);
	    if (record == null) {
	        throw new UnknownRecordException("Unknown bg academic recognition...");
	    }
	    NacidDataProvider provider = getNacidDataProvider();
	    AcademicRecognitionDataProvider dp = provider.getAcademicRecognitionDataProvider();
	    dp.saveBGAcademicRecognitionRecord(record);   
        NomenclaturesDataProvider nomProvider = provider.getNomenclaturesDataProvider();
         
        FlatNomenclature flCanceled = nomProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, "Отменена");
	    record.setSimilarRecognitions(dp.getSimilarAcademicRecognitions(record.getApplicant(), record.getUniversity(), 
	    		record.getUniversityCountry(), record.getEducationLevel(), record.getDiplomaSpeciality(), record.getId()));
	    
	    for(BGAcademicRecognitionInfoImpl extended :record.getSimilarRecognitions()){
	    	if(extended.getId().equals(record.getRelatedRecognitionId())){
	    		BGAcademicRecognitionInfoImpl simrecord = new BGAcademicRecognitionInfoImpl(); 
	    		simrecord.setApplicant(extended.getApplicant());
	    		simrecord.setCitizenship(extended.getCitizenship());
	    		simrecord.setCreatedDate(extended.getCreatedDate());
	    		simrecord.setDenialProtocolNumber(extended.getDenialProtocolNumber());
	    		simrecord.setDiplomaDate(extended.getDiplomaDate());
	    		simrecord.setDiplomaNumber(extended.getDiplomaNumber());
	    		simrecord.setDiplomaSpeciality(extended.getDiplomaSpeciality());
	    		simrecord.setEducationLevel(extended.getEducationLevel());
	    		simrecord.setId(extended.getId());
	    		simrecord.setInputNumber(extended.getInputNumber());
	    		simrecord.setNotes(extended.getNotes());
	    		simrecord.setOutputNumber(extended.getOutputNumber());
	    		simrecord.setProtocolNumber(extended.getProtocolNumber());
	    		simrecord.setRecognizedSpeciality(extended.getRecognizedSpeciality());
	    		simrecord.setRecognizedUniversityId(extended.getRecognizedUniversityId());
	    		simrecord.setRelatedRecognitionId(extended.getRelatedRecognitionId());
	    		simrecord.setUniversity(extended.getUniversity());
	    		simrecord.setUniversityCountry(extended.getUniversityCountry());
	    		simrecord.setRecognitionStatusId(flCanceled.getId());
	    		dp.saveBGAcademicRecognitionRecord(simrecord);
	    	}
	    }
	    request.setAttribute(attributeName, record);
	    request.setAttribute(similarName, record.getSimilarRecognitions());
	    request.getSession().removeAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION);
	    resetTableData(request);
	    request.setAttribute(WebKeys.NEXT_SCREEN, nextScreen);
	    
	    SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
	    request.setAttribute(WebKeys.SYSTEM_MESSAGE, systemMessageWebmodel);
	    generateBGUniversitiesCombobox(record.getRecognizedUniversityId(), getNacidDataProvider().getUniversityDataProvider(), request);
	    ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, nomProvider,
				true, request, recognitionStatuses, null, false);
	}

	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int id = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (id <= 0) {
	      throw new UnknownRecordException("Unknown id:" + id);
	    }
	    AcademicRecognitionDataProvider unDP = getNacidDataProvider().getAcademicRecognitionDataProvider();
	    unDP.deleteBGAcademicRecognitionRecord(id);
	    request.getSession().removeAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION);
	    handleList(request, response);
	}
	
	private void resetTableData(HttpServletRequest request) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		AcademicRecognitionDataProvider dp = nacidDataProvider.getAcademicRecognitionDataProvider();
		List<BGAcademicRecognitionInfoImpl> acRecognitions = dp.getAcademicRecognitions();
		if (acRecognitions != null) {
			for (BGAcademicRecognitionInfo acRec : acRecognitions) {
				try {
					
					table.addRow(acRec.getId(), acRec.getApplicant(), acRec.getCitizenship(), acRec.getUniversity(), acRec.getUniversityCountry(), acRec.getEducationLevel(),
							acRec.getDiplomaSpeciality(), acRec.getDiplomaNumber(), acRec.getDiplomaDate(),
							acRec.getProtocolNumber(), acRec.getDenialProtocolNumber(), 
							acRec.getRecognizedSpeciality(), acRec.getInputNumber(), acRec.getOutputNumber(), acRec.getRecognizedUniversityId());
				} catch (IllegalArgumentException e) {
				    throw Utils.logException(e);
				} catch (CellCreationException e) {
				    throw Utils.logException(e);
				}
			}
		}
	}

	private static FilterWebModel generateTextFieldFilter(HttpServletRequest request, String filterName, String columnName, boolean labelOnTop, String elementClass) {
		TextFieldFilterWebModel res = new TextFieldFilterWebModel(filterName, columnName, request.getParameter(filterName));
		res.setLabelOnTop(labelOnTop);
		if (!StringUtils.isEmpty(elementClass)) {
			res.setElementClass(elementClass);
		}

		return res;
	}

	static FilterWebModel generateBGUniversitiesFilterWebModel(Integer activeUniId, NacidDataProvider nacidDataProvider, HttpServletRequest request) {
		ComboBoxWebModel combobox = generateBGUniversitiesCombobox(activeUniId, nacidDataProvider.getUniversityDataProvider(), null);
		ComboBoxFilterWebModel result = new ComboBoxFilterWebModel(combobox, FILTER_NAME_BG_UNIVERSITY, COLUMN_NAME_BG_UNIVERSITY_ID);
		result.setElementClass("brd w300");
		result.setLabelOnTop(true);
		return result;
	}
	
	public static ComboBoxWebModel generateBGUniversitiesCombobox(Integer selectedUniversityId, UniversityDataProvider udp, HttpServletRequest request) {
	    List<University> universities = udp.getUniversities(Country.COUNTRY_ID_BULGARIA, false);
	    ComboBoxWebModel model = new ComboBoxWebModel(selectedUniversityId == null ? null : selectedUniversityId.toString(), true);
	    for (University u:universities) {
	        model.addItem(u.getId() + "", u.getBgName() + ", " + u.getCity());
	    }
	    if (request != null) {
			request.setAttribute("recognizedUniversities", model);
		}
	    return model;

	    //ComboBoxUtils.generateComboBox(selectedUniversityId, universities, request, "recognizedUniversities", true, "getId", "getBgName");
	}
	
}
