package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.UniversityFacultyWebModel;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UniversityHandler extends NacidBaseRequestHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_COUNTRY = "Държава";
	private static final String COLUMN_NAME_COUNTRY_ID = "countryId";
	private static final String COLUMN_NAME_BG_NAME = "Наименование";
	private static final String COLUMN_NAME_ORG_NAME = "Оригинално наименование";
	private static final String COLUMN_NAME_CITY = "Град";
	private static final String COLUMN_NAME_ADDRESS = "Адрес";
	private static final String COLUMN_NAME_PHONE = "Телефон";
	private static final String COLUMN_NAME_FAX = "Факс";
	//private static final String COLUMN_NAME_EMAIL = "Електронна поща";
	//private static final String COLUMN_NAME_WEB_SITE = "Интернет страница";
	//private static final String COLUMN_NAME_URL = "url dipl reg";
	private static final String COLUMN_NAME_TO_DATE = "До дата";
	
	private static final String FILTER_NAME_COUNTRY = "countryFilter";
    private static final String FILTER_NAME_BG_NAME = "bgNameFilter";
    private static final String FILTER_NAME_ORG_NAME = "orgNameFilter";

	public UniversityHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
		
		generateCountryComboBox(0, nDP, request);
		generateGenericNameComboBox(null, nDP, request);

		request.setAttribute(WebKeys.UNIVERSITY_WEB_MODEL, new UniversityWebModel(null));
		request.setAttribute(WebKeys.NEXT_SCREEN, "university_edit");
	}

	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		
		int universityId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (universityId <= 0) {
			throw new UnknownRecordException("Unknown university ID:" + universityId);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, "university_edit");
		NacidDataProvider nacidDataProvider = getNacidDataProvider();

		UniversityDataProvider unvDP = nacidDataProvider.getUniversityDataProvider();
		NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
		
		University university = unvDP.getUniversity(universityId);
		
		if (university == null) {
			throw new UnknownRecordException("Unknown university ID:" + universityId);
		}
		generateCountryComboBox(university.getCountryId(), nomDP, request);
		generateGenericNameComboBox(university.getGenericNameId(), nomDP, request);
		request.setAttribute(WebKeys.UNIVERSITY_WEB_MODEL, new UniversityWebModel(university));
		List<UniversityFaculty>  faculties = unvDP.getUniversityFaculties(universityId, null, false);
		if (faculties != null) {
			request.setAttribute(WebKeys.UNIVERSITY_FACULTIES_WEB_MODEL, faculties.stream().map(UniversityFacultyWebModel::new).collect(Collectors.toList()));
		}


	}

	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_UNIVERSITY);

		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_COUNTRY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_BG_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_ORG_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_CITY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_ADDRESS, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_PHONE, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_FAX, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_WEB_SITE, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_URL, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_TO_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
			
			session.setAttribute(WebKeys.TABLE_UNIVERSITY, table);
			resetTableData(request);

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_UNIVERSITY + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
			TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_COUNTRY, COLUMN_NAME_COUNTRY_ID, request, table, tableState);
            TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_BG_NAME, COLUMN_NAME_BG_NAME, request, table, tableState);
            TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_ORG_NAME, COLUMN_NAME_ORG_NAME, request, table, tableState);
			session.setAttribute(WebKeys.TABLE_UNIVERSITY + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на " + MessagesBundle.getMessagesBundle().getValue("universities"));
		// webmodel.setColumnFormatter("userDate",
		// CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		webmodel.hideUnhideColumn(COLUMN_NAME_COUNTRY_ID, true);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_UNIVERSITY + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();




            TextFieldFilterWebModel tfFWB = new TextFieldFilterWebModel(FILTER_NAME_BG_NAME, COLUMN_NAME_BG_NAME, request.getParameter(FILTER_NAME_BG_NAME));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd");
            filtersWebModel.addFiler(tfFWB);

            tfFWB = new TextFieldFilterWebModel(FILTER_NAME_ORG_NAME, COLUMN_NAME_ORG_NAME, request.getParameter(FILTER_NAME_ORG_NAME));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd");
            filtersWebModel.addFiler(tfFWB);



            filtersWebModel.addFiler(generateCountryFilterWebModel(DataConverter.parseInt(request.getParameter(FILTER_NAME_COUNTRY), 0), getNacidDataProvider().getNomenclaturesDataProvider(), request));
			filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
		    session.setAttribute(WebKeys.TABLE_UNIVERSITY
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
		int countryId = DataConverter.parseInt(request.getParameter("country"), 0);
		String bgName = request.getParameter("bgName");
		String orgName = request.getParameter("orgName");
		String city = request.getParameter("city");
		String addrDetails = request.getParameter("addrDetails");
		String phone = request.getParameter("phone");
		String fax = request.getParameter("fax");
		String email = request.getParameter("email");
		String webSite = DataConverter.convertUrl(request.getParameter("webSite"));
		String urlDiplomaRegister = DataConverter.convertUrl(request.getParameter("urlDiplomaRegister"));
		String dateFrom = request.getParameter("dateFrom");
		String dateTo = request.getParameter("dateTo");
		Integer genericNameId = DataConverter.parseInteger(request.getParameter("genericNameId"), null);
		
		Date dateFromDate = null;
		if(dateFrom != null) {
			dateFromDate = DataConverter.parseDate(dateFrom);
		}
		if (id == 0 && dateFromDate == null) {
			dateFromDate = Utils.getToday();
		}
		
		Date dateToDate = null;
		if(dateTo != null) {
			dateToDate = DataConverter.parseDate(dateTo);
		}
		
		UniversityDataProvider unvDP = getNacidDataProvider().getUniversityDataProvider();
		
		if (id != 0 && unvDP.getUniversity(id) == null) {
			throw new UnknownRecordException("Unknown university ID:" + id);
		}
		
		 
		
		int newId = unvDP.saveUniversity(id, countryId, bgName, orgName, city, addrDetails, phone, fax, email, webSite, urlDiplomaRegister,
				dateFromDate, dateToDate, genericNameId);
		List<DiplomaType> changedDiplomaTypes = null;
		// Ako ima zadadena dateTo i se editva star universitet, togava se
		// obhojdat vsi4ki diplomaTypes i ako diplomaType-a e validen kym
		// dateTo, mu se promenq dateTo-to
		if (dateToDate != null && id != 0) {
			DiplomaTypeDataProvider diplomaTypeDataProvider = getNacidDataProvider().getDiplomaTypeDataProvider();
			changedDiplomaTypes = diplomaTypeDataProvider.changeDiplomaTypeDateToByUniversity(newId, dateToDate);
		}
		SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата",
				SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
		//TODO: da napravq nov DiplomaTypeNeshtoSiWebModel v koito da naslagam promenenite diplomaTypes, zashtoto ne izglejda dobre da izlizat kato SystemMessage
		
		if (changedDiplomaTypes != null) {
			List<String> changedDiplomaTypesString = new ArrayList<String>();
			changedDiplomaTypesString.add("\"До дата\" на следните видове дипломи беше променена на " + DataConverter.formatDate(dateToDate));
			int i = 1;
			for (DiplomaType dt : changedDiplomaTypes) {
				List<DiplomaTypeIssuer> issuers = dt.getDiplomaTypeIssuers();
				List<String> issuerNames = new ArrayList<String>();
				if (issuers != null) {
					for (DiplomaTypeIssuer issuer : issuers) {
						University u = issuer.getUniversity();
						if (u != null) {
							Country c = u.getCountry();
							issuerNames.add(u.getBgName() + (c != null ? " - " + c.getName() : "") );
						}
					}
				}
				changedDiplomaTypesString.add((i++) + ". " + dt.getTitle()
						+ (issuerNames.size() > 0 ? " - Висши училища:" + StringUtils.join(issuerNames, ", ") : ""));
			}
			request.setAttribute("changedDiplomaTypes", changedDiplomaTypesString);
		}
		request.setAttribute(WebKeys.SYSTEM_MESSAGE, systemMessageWebmodel);

		resetTableData(request);
		request.setAttribute(WebKeys.UNIVERSITY_WEB_MODEL, new UniversityWebModel(unvDP.getUniversity(newId)));

		NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
				
		generateCountryComboBox(countryId, nomDP, request);
		generateGenericNameComboBox(genericNameId, nomDP, request);
		
		request.setAttribute(WebKeys.NEXT_SCREEN, "university_edit");
		request.getSession().removeAttribute(WebKeys.TABLE_UNIVERSITY);
		
	}

	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int universityId = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (universityId <= 0) {
	      throw new UnknownRecordException("Unknown university Id:" + universityId);
	    }
	    
	    UniversityDataProvider unDP = getNacidDataProvider().getUniversityDataProvider();
	    unDP.disableUniversity(universityId);
	    
	    request.getSession().removeAttribute(WebKeys.TABLE_UNIVERSITY);
	    handleList(request, response);
	}
	
	private void resetTableData(HttpServletRequest request) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_UNIVERSITY);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		UniversityDataProvider unvDP = nacidDataProvider.getUniversityDataProvider();
		NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
		
		List<University> universities = unvDP.getAllUniversities();
		
		if (universities != null) {
			for (University u : universities) {
				
				Country country = nomDP.getCountry(u.getCountryId());
				String countryName = "";
				if(country != null) {
					countryName = country.getName();
				}
				try {
					
					table.addRow(u.getId(), countryName, u.getCountryId(), u.getBgName(),
							u.getOrgName(), u.getCity(), u.getAddrDetails(),
							u.getPhone(), u.getFax(), /*u.getEmail() , 
							u.getWebSite(), u.getUrlDiplomaRegister()*/
							u.getDateTo());
				} catch (IllegalArgumentException e) {
				    throw Utils.logException(e);
				} catch (CellCreationException e) {
				    throw Utils.logException(e);
				}
			}
		}
	}
	
	private static ComboBoxWebModel generateCountryComboBox(Integer activeCountryId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
		return ComboBoxUtils.generateNomenclaturesComboBox(activeCountryId, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, nomDP, false, request, "countryCombo", OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
	}
	private static ComboBoxWebModel generateGenericNameComboBox(Integer activeGenericNameId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
		return ComboBoxUtils.generateNomenclaturesComboBox(activeGenericNameId, NomenclaturesDataProvider.FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME, nomDP, false, request, "genericNameCombo", OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
	}
	static FilterWebModel generateCountryFilterWebModel(Integer activeCountryId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
		ComboBoxWebModel combobox = generateCountryComboBox(activeCountryId, nomDP, null);
		ComboBoxFilterWebModel result = new ComboBoxFilterWebModel(combobox, FILTER_NAME_COUNTRY, COLUMN_NAME_COUNTRY);
		result.setElementClass("brd w300");
        result.setLabelOnTop(true);
		return result;
	}
}
