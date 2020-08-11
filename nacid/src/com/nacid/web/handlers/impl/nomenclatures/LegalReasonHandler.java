package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.LegalReason;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.model.nomenclatures.LegalReasonWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class LegalReasonHandler extends NacidBaseRequestHandler {
	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_NAME = "Наименование";
	private static final String COLUMN_NAME_APP_STATUS_ID = "Статус на заявлението";
	private static final String COLUMN_NAME_DATE_FROM = "От дата";
	private static final String COLUMN_NAME_DATE_TO = "До дата";


	public LegalReasonHandler(ServletContext servletContext) {
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
		Integer applicationStatusId = DataConverter.parseInteger(request.getParameter("app_status_id"), null);

		String ordinanceArticle = DataConverter.parseString(request.getParameter("ordinance_article"), null);
		String regulationArticle = DataConverter.parseString(request.getParameter("regulation_article"), null);
		String regulationText = DataConverter.parseString(request.getParameter("regulation_text"), null);

		Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));

		String[] appTypes = request.getParameterValues("application_type");
		List<Integer> appTypeIds = appTypes == null || appTypes.length == 0 ? new ArrayList<>() : RequestParametersUtils.convertRequestParameterToIntegerList(Arrays.stream(appTypes).collect(Collectors.joining(";")));


		//If new record is added and dateFrom is not set, then dateFrom is set to today
		if (id == 0 && dateFrom == null) {
			dateFrom = Utils.getToday();
		}
		Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
		if (id != 0 && nomenclaturesDataProvider.getLegalReason(id) == null) {
			throw new UnknownRecordException("Unknown Legal Reason ID:" + id);
		}
		if (name == null || "".equals(name)) {
			SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
			webmodel.addAttribute("- грешно въведено име!");
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
			request.setAttribute(WebKeys.LEGAL_REASON, new LegalReasonWebModel(id, name, request.getParameter("ordinance_article"), request.getParameter("regulation_article"), request.getParameter("regulation_text"), request.getParameter("dateFrom"), request.getParameter("dateTo"), getGroupName(request)) );
		} else {
			int newId = nomenclaturesDataProvider.saveLegalReason(id, name, applicationStatusId, ordinanceArticle, regulationArticle, regulationText, dateFrom, dateTo, appTypeIds);
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
			request.setAttribute(WebKeys.LEGAL_REASON, new LegalReasonWebModel(nomenclaturesDataProvider.getLegalReason(newId), getGroupName(request)));
		}
		ComboBoxUtils.generateNomenclaturesComboBox(applicationStatusId, NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS, nomenclaturesDataProvider, false, request, "appStatusCombo", null, true);
		request.setAttribute("activeApplicationTypes", appTypeIds.stream().collect(Collectors.toMap(Function.identity(), Function.identity())));
		request.getSession().removeAttribute(WebKeys.TABLE_LEGAL_REASON);
		request.setAttribute(WebKeys.NEXT_SCREEN, "legal_reason_edit");


	}

	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(WebKeys.NEXT_SCREEN, "legal_reason_edit");
		request.setAttribute(WebKeys.LEGAL_REASON, new LegalReasonWebModel(getGroupName(request)));
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "appStatusCombo", null, true);
	}

	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int legalReasonId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (legalReasonId <= 0) {
			throw new UnknownRecordException("Unknown Legal Reason ID:" + legalReasonId);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, "legal_reason_edit");
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		LegalReason legalReason = nomenclaturesDataProvider.getLegalReason(legalReasonId);
		if (legalReason == null) {
			throw new UnknownRecordException("Unknown Legal Reason ID:" + legalReasonId);
		}
		ComboBoxUtils.generateNomenclaturesComboBox(legalReason.getApplicationStatusId(), NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "appStatusCombo", null, true);
		request.setAttribute(WebKeys.LEGAL_REASON, new LegalReasonWebModel(legalReason, getGroupName(request)));

		List<Integer> appTypes = getNacidDataProvider().getNomenclaturesDataProvider().getApplicationTypesPerLegalReason(legalReasonId);
		request.setAttribute("activeApplicationTypes", appTypes.stream().collect(Collectors.toMap(Function.identity(), Function.identity())));

	}

	public void handleList(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_LEGAL_REASON);
		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		/**
		 * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
		 */
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_APP_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
			table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
			session.setAttribute(WebKeys.TABLE_LEGAL_REASON, table);
			resetTableData(request);

		}
		TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_LEGAL_REASON + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
			session.setAttribute(WebKeys.TABLE_LEGAL_REASON + WebKeys.TABLE_STATE, tableState);
		}

		TableWebModel webmodel = new TableWebModel("Правно основание");
		//webmodel.setColumnFormatter("userDate", CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		if (DataConverter.parseBoolean(request.getParameter("inner"))) {
			request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");
		} else {
			request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");	
		}


		//Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_LEGAL_REASON + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
			session.setAttribute(WebKeys.TABLE_LEGAL_REASON + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}
	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int legalReasonId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (legalReasonId <= 0) {
			throw new UnknownRecordException("Unknown Legal Reason ID:" + legalReasonId);
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		LegalReason legalReason = nomenclaturesDataProvider.getLegalReason(legalReasonId);
		if (legalReason == null) {
			throw new UnknownRecordException("Unknown Legal Reason ID:" + legalReasonId);
		}
		nomenclaturesDataProvider.saveLegalReason(legalReason.getId(), legalReason.getName(), legalReason.getApplicationStatusId(),
				legalReason.getOrdinanceArticle(), legalReason.getRegulationArticle(), legalReason.getRegulationText(),
				legalReason.getDateFrom(), new Date(), nomenclaturesDataProvider.getApplicationTypesPerLegalReason(legalReasonId));
		request.getSession().removeAttribute(WebKeys.TABLE_LEGAL_REASON);
		handleList(request, response);
	}

	private void resetTableData(HttpServletRequest request) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_LEGAL_REASON);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		List<LegalReason> legalReasons = nomenclaturesDataProvider.getLegalReasons(0, null,  OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, false), null);
		if (legalReasons != null) {
			for (LegalReason c:legalReasons) {
				try {
					FlatNomenclature appStatus = c.getApplicationStatus();
					table.addRow(c.getId(), c.getName(), appStatus == null ? "" : appStatus.getName(), c.getDateFrom(),  c.getDateTo());
				} catch (IllegalArgumentException e) {
					throw Utils.logException(e);
				} catch (CellCreationException e) {
					throw Utils.logException(e);
				}
			}
		}
	}
}
