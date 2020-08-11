package com.nacid.regprof.web.handlers.impl.nomenclatures;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.ProfessionExperienceDocumentType;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.regprof.web.model.nomenclatures.ProfessionExperienceDocumentTypeWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;


public class PorfessionExperienceDocumentTypeHandler extends RegProfBaseRequestHandler {
	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_NAME = "Наименование";
	private static final String COLUMN_NAME_IS_FOR_EXPERIENCE_CALCULATION = "Влиза за изчисление на стажа";
	private static final String COLUMN_NAME_DATE_FROM = "От дата";
	private static final String COLUMN_NAME_DATE_TO = "До дата";

	public PorfessionExperienceDocumentTypeHandler(ServletContext servletContext) {
		super(servletContext);
	}


	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
			new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
			return;
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		int id = DataConverter.parseInt(request.getParameter("id"), 0);
		String name = request.getParameter("name");
		boolean isForExperienceCalculation = DataConverter.parseBoolean(request.getParameter("is_for_experience_calculation"));
		
		Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
		
		//If new record is added and dateFrom is not set, then dateFrom is set to today
		if (id == 0 && dateFrom == null) {
			dateFrom = Utils.getToday();
		}
		Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
		if (id != 0 && nomenclaturesDataProvider.getDocumentType(id) == null) {
			throw new UnknownRecordException("Unknown Document Type ID:" + id);
		}
		//Teoretichno tezi proverki se pravqt client side i se razbrahme da nqma server side proverki, no tozi kod e star(predi da se razberem tova neshto)
		//i sym go ostavil, zashtoto taka ili inache si vyr6i rabota
		if (name == null || "".equals(name)) {
			SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
			if (name == null || "".equals(name)) {
				webmodel.addAttribute("- грешно въведено име!");  
			}
			
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
			request.setAttribute(WebKeys.DOCUMENT_TYPE, new ProfessionExperienceDocumentTypeWebModel(id + "", name, isForExperienceCalculation, request.getParameter("dateFrom"), request.getParameter("dateTo")));
			
			generateIsForExperienceCalculationRadio(request, isForExperienceCalculation);
			
		
		} else {
			int newId = nomenclaturesDataProvider.saveProfessionExperienceDocumentType(id, name, isForExperienceCalculation, dateFrom, dateTo);
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
			generateProfessionExperienceDocumentTypeWebModel(request, nomenclaturesDataProvider, nomenclaturesDataProvider.getProfessionExperienceDocumentType(newId));
			request.getSession().removeAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE);
			
		}
		
		request.setAttribute(WebKeys.NEXT_SCREEN, "profession_experience_document_type_edit");


	}

	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		generateProfessionExperienceDocumentTypeWebModel(request, getNacidDataProvider().getNomenclaturesDataProvider(), null);
		request.setAttribute(WebKeys.NEXT_SCREEN, "profession_experience_document_type_edit");
	}

	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int id = DataConverter.parseInt(request.getParameter("id"), -1);
		if (id <= 0) {
			throw new UnknownRecordException("Unknown Document Type ID:" + id);
		}

		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		
		ProfessionExperienceDocumentType documentType = nomenclaturesDataProvider.getProfessionExperienceDocumentType(id);
		if (documentType == null) {
			throw new UnknownRecordException("Unknown DocumentType ID:" + id);
		}
		generateProfessionExperienceDocumentTypeWebModel(request, nomenclaturesDataProvider, documentType);
		request.setAttribute(WebKeys.NEXT_SCREEN, "profession_experience_document_type_edit");
	}
	
	private static void generateProfessionExperienceDocumentTypeWebModel(HttpServletRequest request, NomenclaturesDataProvider nomenclaturesDataProvider, ProfessionExperienceDocumentType documentType) {
		generateIsForExperienceCalculationRadio(request, documentType == null ? false : documentType.isForExperienceCalculation());
		if (documentType != null) {
			request.setAttribute(WebKeys.DOCUMENT_TYPE, new ProfessionExperienceDocumentTypeWebModel(documentType));	
		}
	}

	public void handleList(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE);
		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		/**
		 * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
		 */
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_IS_FOR_EXPERIENCE_CALCULATION, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
			table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
			table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
			session.setAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE, table);
			resetTableData(request);

		}
		TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);

			session.setAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE + WebKeys.TABLE_STATE, tableState);
		}

		TableWebModel webmodel = new TableWebModel("Типове документи за стаж");
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		if (DataConverter.parseBoolean(request.getParameter("inner"))) {
			request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");
		} else {
			request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");	
		}


		//Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));

			session.setAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}
	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int documentTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (documentTypeId <= 0) {
			throw new UnknownRecordException("Unknown Document Type ID:" + documentTypeId);
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		ProfessionExperienceDocumentType documentType = nomenclaturesDataProvider.getProfessionExperienceDocumentType(documentTypeId);
		if (documentType == null) {
			throw new UnknownRecordException("Unknown Document Type ID:" + documentTypeId);
		}
		nomenclaturesDataProvider.saveProfessionExperienceDocumentType(documentType.getId(), documentType.getName(), documentType.isForExperienceCalculation(), documentType.getDateFrom(), new Date());
		request.getSession().removeAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE);
		handleList(request, response);
	}

	private void resetTableData(HttpServletRequest request) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		List<ProfessionExperienceDocumentType> documentTypes = nomenclaturesDataProvider.getProfessionExperienceDocumentTypes(null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false));
		if (documentTypes != null) {
			for (ProfessionExperienceDocumentType c:documentTypes) {
				try {
					table.addRow(c.getId(), c.getName(), c.isForExperienceCalculation(), c.getDateFrom(),  c.getDateTo());
				} catch (IllegalArgumentException e) {
					throw Utils.logException(e);
				} catch (CellCreationException e) {
					throw Utils.logException(e);
				}
			}
		}
	}

	
	private static void generateIsForExperienceCalculationRadio(HttpServletRequest request, boolean isForExperienceCalculation) {
		ComboBoxWebModel rbwm = new ComboBoxWebModel(isForExperienceCalculation + "");
		rbwm.addItem("true", "да");
		rbwm.addItem("false", "не");
		request.setAttribute("isForExperienceCalculationCombo", rbwm);
	}
}
