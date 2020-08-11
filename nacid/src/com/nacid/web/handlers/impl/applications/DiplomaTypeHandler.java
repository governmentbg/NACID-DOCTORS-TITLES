package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.DiplomaTypeRecordForList;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.ajax.DiplomaTypeUniversityFacultyHandler;
import com.nacid.web.handlers.impl.ajax.OriginalEducationLevelAjaxHandler;
import com.nacid.web.model.applications.DiplomaTypeWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class DiplomaTypeHandler extends NacidBaseRequestHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_TITLE = "Заглавие";
	private static final String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
	private static final String COLUMN_NAME_EDU_LEVEL_ID= "eduLevelId";
	private static final String COLUMN_NAME_EDU_LEVEL= "Образователна степен";
	private static final String COLUMN_NAME_VISUAL_ELEM = "Визуални елементи";
	private static final String COLUMN_NAME_PROTECTION_ELEM = "Защитни елементи";
	private static final String COLUMN_NAME_NUMBER_FORMAT_ELEM = "Формат на номера";
	private static final String COLUMN_NAME_COUNTRY = "Държава";
	private static final String COLUMN_DATE_FROM = "От дата";
	private static final String COLUMN_DATE_TO = "До дата";
	private static final String JOINT_DEGREE = "съвместна образователна степен";
	private static final String FILTER_NAME_COUNTRY = "countryFilter";
	private static final String FILTER_NAME_EDU_LEVEL = "eduLevelFilter";
	
	//private ServletContext servletContext;
	
	public DiplomaTypeHandler(ServletContext servletContext) {
		super(servletContext);
		//this.servletContext = servletContext;
	}

	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		generateCobmoboxWebModels(null, null, nacidDataProvider, request);


        DiplomaTypeWebModel webmodel = new DiplomaTypeWebModel(null);
		addUniversitiesToWebModel(nacidDataProvider, webmodel, null);;
		request.setAttribute(WebKeys.DIPLOMA_TYPE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_edit");
	}

	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		
		int diplomaTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (diplomaTypeId <= 0) {
			throw new UnknownRecordException("Unknown diplomatype ID:" + diplomaTypeId);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_edit");
		NacidDataProvider nacidDataProvider = getNacidDataProvider();

		DiplomaTypeDataProvider dtDP = nacidDataProvider.getDiplomaTypeDataProvider();
		
		DiplomaType diplomaType = dtDP.getDiplomaType(diplomaTypeId);
		
		if (diplomaType == null) {
			throw new UnknownRecordException("Unknown university ID:" + diplomaTypeId);
		}

		Set<UniversityIdWithFacultyId> uniIds = diplomaType.getUniversityWithFacultyIds();
        DiplomaTypeWebModel webmodel = new DiplomaTypeWebModel(diplomaType);
		addUniversitiesToWebModel(nacidDataProvider, webmodel, uniIds);
		request.setAttribute(WebKeys.DIPLOMA_TYPE_WEB_MODEL, webmodel);

        generateCobmoboxWebModels(diplomaType, uniIds, nacidDataProvider, request);


		new DiplomaTypeAttachmentHandler(getServletContext()).handleList(request, response);
	}


    private static void generateCobmoboxWebModels(DiplomaType diplomaType, Set<UniversityIdWithFacultyId> uniIds, NacidDataProvider nacidDataProvider, HttpServletRequest request) {

        Integer uniId = uniIds == null || uniIds.size() == 0 ? null : uniIds.stream().findFirst().get().getUniversityId();
        University u = uniId == null ? null : nacidDataProvider.getUniversityDataProvider().getUniversity(uniId);
        Integer countryId = u == null ? null : u.getCountryId();


        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        generateEduLevelComboBox(diplomaType == null ? null : diplomaType.getEduLevelId(), nomDP, request);
        generateOriginalEducationLevelCombobox(countryId, nacidDataProvider, diplomaType == null ? null : diplomaType.getOriginalEducationLevelId(), diplomaType == null ? null : diplomaType.getEduLevelId(), request);

        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, nomDP, false, request, "countryCombo", null, true);

        OrderCriteria orderCriteria = OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true);
        List<FlatNomenclature> bolognaCycleNoms = nomDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE, null, orderCriteria);
        List<NationalQualificationsFramework> nqfNoms = nomDP.getNationalQualificationsFrameworks(null, countryId, orderCriteria);
        List<FlatNomenclature> eqfNoms = nomDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK, null, orderCriteria);

        ComboBoxUtils.generateNomenclaturesComboBox(diplomaType == null ? null : diplomaType.getBolognaCycleId(), bolognaCycleNoms, false, request, "bolognaCycleCombo", true);
        ComboBoxUtils.generateNomenclaturesComboBox(diplomaType == null ? null : diplomaType.getNationalQualificationsFrameworkId(), nqfNoms, false, request, "nqfCombo", true);
        ComboBoxUtils.generateNomenclaturesComboBox(diplomaType == null ? null : diplomaType.getEuropeanQualificationsFrameworkId(), eqfNoms, false, request, "eqfCombo", true);


        ComboBoxUtils.generateNomenclaturesComboBox(diplomaType == null ? null : diplomaType.getBolognaCycleAccessId(), bolognaCycleNoms, false, request, "bolognaCycleAccessCombo", true);
        ComboBoxUtils.generateNomenclaturesComboBox(diplomaType == null ? null : diplomaType.getNationalQualificationsFrameworkAccessId(), nqfNoms, false, request, "nqfAccessCombo", true);
        ComboBoxUtils.generateNomenclaturesComboBox(diplomaType == null ? null : diplomaType.getEuropeanQualificationsFrameworkAccessId(), eqfNoms, false, request, "eqfAccessCombo", true);






    }
	
	public void handleView(HttpServletRequest request, HttpServletResponse response){
		 super.handleView(request, response);
		 new DiplomaTypeAttachmentHandler(getServletContext()).handleList(request, response);
	}

	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_DIPLOMA_TYPE);

		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			
			
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_TITLE, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_EDU_LEVEL_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			//table.addColumnHeader(COLUMN_NAME_VISUAL_ELEM, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_PROTECTION_ELEM, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_NUMBER_FORMAT_ELEM, CellValueDef.CELL_VALUE_TYPE_STRING);
			
			table.addColumnHeader(COLUMN_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
			table.addColumnHeader(COLUMN_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
			
			session.setAttribute(WebKeys.TABLE_DIPLOMA_TYPE, table);
			resetTableData(request);

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_DIPLOMA_TYPE + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_COUNTRY, COLUMN_NAME_COUNTRY, request, table, tableState);
			TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_EDU_LEVEL, COLUMN_NAME_EDU_LEVEL_ID, request, table, tableState);
			session.setAttribute(WebKeys.TABLE_DIPLOMA_TYPE + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на типовете дипломи");
		// webmodel.setColumnFormatter("userDate",
		// CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		webmodel.hideUnhideColumn(COLUMN_NAME_EDU_LEVEL_ID, true);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_DIPLOMA_TYPE + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			
			filtersWebModel.addFiler(generateCountryFilterComboBox(request.getParameter(FILTER_NAME_COUNTRY), getNacidDataProvider().getNomenclaturesDataProvider(), request));
			ComboBoxWebModel wm = ComboBoxUtils.generateNomenclaturesComboBox(DataConverter.parseInteger(request.getParameter(FILTER_NAME_EDU_LEVEL), null), NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getNacidDataProvider().getNomenclaturesDataProvider(), false, null, null, null, true);
			FilterWebModel f = new ComboBoxFilterWebModel(wm, FILTER_NAME_EDU_LEVEL, COLUMN_NAME_EDU_LEVEL);
			f.setLabelOnTop(true);
			f.setElementClass("brd w300");
			filtersWebModel.addFiler(f);
			
			session.setAttribute(WebKeys.TABLE_DIPLOMA_TYPE
					+ WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}

	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
			new HomePageHandler(request.getSession().getServletContext())
					.processRequest(request, response);
			return;
		}
		
		int id = DataConverter.parseInt(request.getParameter("id"), 0);
		
		int universitiesCount = DataConverter.parseInt(request.getParameter("universities_count"), 0);
//        Set<Integer> universityIds = null;
		Set<UniversityIdWithFacultyId> universityWithFaculties = null;
        if (universitiesCount > 0) {
			universityWithFaculties = new LinkedHashSet<>();
            for (int i = 0; i < universitiesCount; i++) {
                int currentUniversityId = DataConverter.parseInt(request.getParameter("university_id" + i), -1);
				Integer facultyId = DataConverter.parseInteger(request.getParameter("university_faculty_id" + i), null);
                if (currentUniversityId != -1) {
					universityWithFaculties.add(new UniversityIdWithFacultyId(currentUniversityId, facultyId));
                }
            }
        }
		
		String visualElementsDescr = request.getParameter("visualElementsDescr");
		String protectionElementsDescr = request.getParameter("protectionElementsDescr");
		String numberFormatDescr = request.getParameter("numberFormatDescr");
		String notes = request.getParameter("notes");
		String dateFrom = request.getParameter("dateFrom");
		String dateTo = request.getParameter("dateTo");
		int eduLevelId = DataConverter.parseInt(request.getParameter("eduLevel"), 0);
        Integer originalEducationLevelId = DataConverter.parseInteger(request.getParameter("originalEduLevel"), null);
		String title = request.getParameter("title");
		int type = DataConverter.parseInteger(request.getParameter("type"), null);
		boolean jointDegree = universityWithFaculties != null && universityWithFaculties.size() > 1;



        Integer bolognaCycleId = DataConverter.parseInteger(request.getParameter("bolognaCycleId"), null);
        Integer nationalQualificationsFrameworkId = DataConverter.parseInteger(request.getParameter("nationalQualificationsFrameworkId"), null);
        Integer europeanQualificationsFrameworkId = DataConverter.parseInteger(request.getParameter("europeanQualificationsFrameworkId"), null);
        Integer bolognaCycleAccessId = DataConverter.parseInteger(request.getParameter("bolognaCycleAccessId"), null);
        Integer nationalQualificationsFrameworkAccessId = DataConverter.parseInteger(request.getParameter("nationalQualificationsFrameworkAccessId"), null);
        Integer europeanQualificationsFrameworkAccessId = DataConverter.parseInteger(request.getParameter("europeanQualificationsFrameworkAccessId"), null);

		Date dateFromDate = DataConverter.parseDate(dateFrom);
		if (id == 0 && dateFromDate == null) {
			dateFromDate = Utils.getToday();
		}
		
		Date dateToDate = null;
		if(dateTo != null) {
			dateToDate = DataConverter.parseDate(dateTo);
		}
		
		DiplomaTypeDataProvider dtDP = getNacidDataProvider().getDiplomaTypeDataProvider();
		
		if (id != 0 && dtDP.getDiplomaType(id) == null) {
			throw new UnknownRecordException("Unknown diploma type ID:" + id);
		}
        DiplomaType dt = null;
		DiplomaTypeWebModel webmodel;
		try {
			int newId = dtDP.saveDiplomaType(id, universityWithFaculties, visualElementsDescr, protectionElementsDescr, numberFormatDescr, notes, dateFromDate, dateToDate, title, eduLevelId, originalEducationLevelId, jointDegree,
                    bolognaCycleId, nationalQualificationsFrameworkId, europeanQualificationsFrameworkId, bolognaCycleAccessId, nationalQualificationsFrameworkAccessId, europeanQualificationsFrameworkAccessId, type);
			
			request.setAttribute("diplomaTypeSystemMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
					SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            dt = dtDP.getDiplomaType(newId);
			webmodel = new DiplomaTypeWebModel(dt);
			request.getSession().removeAttribute(WebKeys.TABLE_DIPLOMA_TYPE);
		} catch (DiplomaTypeEditException e) {
			SystemMessageWebModel error = new SystemMessageWebModel();
			error.setAttributes(e.getMessages());
			request.setAttribute("diplomaTypeSystemMessage", error);
			webmodel = new DiplomaTypeWebModel(id, visualElementsDescr, protectionElementsDescr, numberFormatDescr, notes, dateFrom, dateTo, title, jointDegree, type);
			request.setAttribute(WebKeys.DIPLOMA_TYPE_WEB_MODEL, webmodel);
		}
		request.setAttribute(WebKeys.DIPLOMA_TYPE_WEB_MODEL, webmodel);
		
        generateCobmoboxWebModels(dt, universityWithFaculties, getNacidDataProvider(), request);

		addUniversitiesToWebModel(getNacidDataProvider(), webmodel, universityWithFaculties);
		request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_edit");
		new DiplomaTypeAttachmentHandler(getServletContext()).handleList(request, response);
	}

	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int diplomaTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (diplomaTypeId <= 0) {
	      throw new UnknownRecordException("Unknown diplomaType Id:" + diplomaTypeId);
	    }
	    
	    DiplomaTypeDataProvider dtDP = getNacidDataProvider().getDiplomaTypeDataProvider();
	    dtDP.disableDiplomaType(diplomaTypeId);
	    
	    request.getSession().removeAttribute(WebKeys.TABLE_DIPLOMA_TYPE);
	    handleList(request, response);
	}


	
	private void resetTableData(HttpServletRequest request) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_DIPLOMA_TYPE);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		DiplomaTypeDataProvider dtDP = nacidDataProvider.getDiplomaTypeDataProvider();
		NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
		
		List<DiplomaTypeRecordForList> diplomaTypes = dtDP.getAllDiplomaTypes(DiplomaType.TYPE_NORMAL);
		
		if (diplomaTypes != null) {
			for (DiplomaTypeRecordForList dt : diplomaTypes) {
				try {
					List<String> countries = dt.getUniCountries();
                    String c = countries == null || countries.size() == 0 ? "" : (countries.size() == 1 ? countries.get(0) : JOINT_DEGREE);
                    String uniNames = dt.getUniCountries() == null ? "" : StringUtils.join(dt.getUniCountries(), ", <br />");
					table.addRow(dt.getId(), dt.getTitle(), c,  uniNames,
							dt.getEduLevelName(), dt.getEduLevelId(),
							dt.getDateFrom(), dt.getDateTo());
				} catch (IllegalArgumentException e) {
				    throw Utils.logException(e);
				} catch (CellCreationException e) {
				    throw Utils.logException(e);
				}
			}
		}
	}
	
	private static void addUniversitiesToWebModel(NacidDataProvider nacidDataProvider, DiplomaTypeWebModel webmodel, Set<UniversityIdWithFacultyId> uniWithFacultyIds) {
		if (uniWithFacultyIds == null) {
			addUniversityComboBox(null, null, null, nacidDataProvider.getUniversityDataProvider(), webmodel);
			return;
		}
		UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
		for (UniversityIdWithFacultyId uniWithFacultyId : uniWithFacultyIds) {
			University u = universityDataProvider.getUniversity(uniWithFacultyId.getUniversityId());
			UniversityFaculty faculty = uniWithFacultyId.getFacultyId() == null ? null : universityDataProvider.getUniversityFaculty(uniWithFacultyId.getFacultyId());
			addUniversityComboBox(u.getId(), uniWithFacultyId.getFacultyId(), u.getCountryId(), universityDataProvider, webmodel);
		}
	}
	public static void addUniversityComboBox(Integer activeUniversityId, Integer activeFacultyId, Integer countryId, UniversityDataProvider unvDP,
            DiplomaTypeWebModel webmodel) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeUniversityId == null ? null : activeUniversityId + "", true);
        List<University> universities = countryId == null ? null : unvDP.getUniversities(countryId, false);
        if (universities != null) {
            for (University u : universities) {

            	combobox.addItem(u.getId() + "", u.getBgName() + (u.isActive() ? "" : " (inactive)"));
            }
        }
		ComboBoxWebModel facultyWebModel = DiplomaTypeUniversityFacultyHandler.generateUniversityFacultyComboBox(activeUniversityId, activeFacultyId, unvDP, null);
        webmodel.addDiplomaTypeIssuer(countryId, combobox, facultyWebModel);
    }
	
	private static void generateEduLevelComboBox(Integer activeEduLevelId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
		ComboBoxWebModel combobox = new ComboBoxWebModel(activeEduLevelId == null ? null : activeEduLevelId + "", true);
		List<FlatNomenclature> levels = nomDP.getFlatNomenclatures(
				NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
		if (levels != null) {
			for (FlatNomenclature l : levels) {
				combobox.addItem(l.getId() + "", l.getName() +
						(l.isActive() ? "" : " (inactive)"));
			}
			request.setAttribute("eduLevelCombo", combobox);
		}
	}

    private static void generateOriginalEducationLevelCombobox(Integer countryId, NacidDataProvider nacidDataProvider, Integer originalEducationLevel, Integer eduLevelId, HttpServletRequest request){
        OriginalEducationLevelAjaxHandler.generateOriginalEducationLevelsByCountryComboBox(null, originalEducationLevel, countryId == null ? 0 : countryId, eduLevelId == null ? 0 : eduLevelId, nacidDataProvider.getNomenclaturesDataProvider(), request);
    }


    private static ComboBoxFilterWebModel generateCountryFilterComboBox(String activeCountryName,
            NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryName, true);
        combobox.addItem(JOINT_DEGREE, JOINT_DEGREE);
        List<Country> countries = nomenclaturesDataProvider.getCountries(null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        if (countries != null) {
            for (Country c : countries) {
                combobox.addItem(c.getName() + "", c.getName() + (c.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_COUNTRY, COLUMN_NAME_COUNTRY);
        res.setElementClass("brd w400");
        res.setLabelOnTop(true);
        return res;
    }
}
