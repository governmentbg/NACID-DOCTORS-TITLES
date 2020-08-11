package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ExpertPosition;
import com.nacid.bl.nomenclatures.FlatNomenclature;
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
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

import static com.nacid.web.handlers.impl.nomenclatures.FlatNomenclatureHandler.refreshCachedNomenclatures;


public class ExpertPositionHandler extends NacidBaseRequestHandler {
	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_NAME = "Наименование";
	private static final String COLUMN_NAME_APP_STATUS_ID = "Статус на заявлението";
	private static final String COLUMN_NAME_DATE_FROM = "От дата";
	private static final String COLUMN_NAME_DATE_TO = "До дата";


	public ExpertPositionHandler(ServletContext servletContext) {
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
		Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
		//If new record is added and dateFrom is not set, then dateFrom is set to today
		if (id == 0 && dateFrom == null) {
			dateFrom = Utils.getToday();
		}
		Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
		if (id != 0 && nomenclaturesDataProvider.getExpertPosition(id) == null) {
			throw new UnknownRecordException("Unknown Expert Position ID:" + id);
		}
		if (name == null || "".equals(name)) {
			SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
			webmodel.addAttribute("- грешно въведено име!");
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
			request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(id, name, request.getParameter("dateFrom"), request.getParameter("dateTo"), getGroupName(request), null, NomenclaturesDataProvider.NOMENCLATURE_EXPERT_POSITION) );

		} else {
			int newId = nomenclaturesDataProvider.saveExpertPosition(id, name, applicationStatusId, dateFrom, dateTo);
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
			request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(nomenclaturesDataProvider.getExpertPosition(newId), getGroupName(request), null));
		}
		ComboBoxUtils.generateNomenclaturesComboBox(applicationStatusId, NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS, nomenclaturesDataProvider, false, request, "appStatusCombo", null, true);
		request.getSession().removeAttribute(WebKeys.TABLE_EXPERT_POSITION);
		request.setAttribute(WebKeys.NEXT_SCREEN, "expert_position_edit");

		refreshCachedNomenclatures(request);

	}

	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(WebKeys.NEXT_SCREEN, "expert_position_edit");
		request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(null, null, NomenclaturesDataProvider.NOMENCLATURE_EXPERT_POSITION));
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "appStatusCombo", null, true);
	}

	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int expertPositionId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (expertPositionId <= 0) {
			throw new UnknownRecordException("Unknown Expert Position ID:" + expertPositionId);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, "expert_position_edit");
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		ExpertPosition expertPosition = nomenclaturesDataProvider.getExpertPosition(expertPositionId);
		if (expertPosition == null) {
			throw new UnknownRecordException("Unknown Expert Position ID:" + expertPositionId);
		}
		ComboBoxUtils.generateNomenclaturesComboBox(expertPosition.getRelatedAppStatusId(), NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "appStatusCombo", null, true);
		request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(expertPosition, getGroupName(request), null));
	}

	public void handleList(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_EXPERT_POSITION);
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
			session.setAttribute(WebKeys.TABLE_EXPERT_POSITION, table);
			resetTableData(request);

		}
		TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_EXPERT_POSITION + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
			session.setAttribute(WebKeys.TABLE_EXPERT_POSITION + WebKeys.TABLE_STATE, tableState);
		}

		TableWebModel webmodel = new TableWebModel("Становище");
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
		FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_EXPERT_POSITION + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
			session.setAttribute(WebKeys.TABLE_EXPERT_POSITION + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}
	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int expertPositionId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (expertPositionId <= 0) {
			throw new UnknownRecordException("Unknown Expert Position ID:" + expertPositionId);
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		ExpertPosition expertPosition = nomenclaturesDataProvider.getExpertPosition(expertPositionId);
		if (expertPosition == null) {
			throw new UnknownRecordException("Unknown Expert Position ID:" + expertPositionId);
		}
		nomenclaturesDataProvider.saveExpertPosition(expertPosition.getId(), expertPosition.getName(), expertPosition.getRelatedAppStatusId(), expertPosition.getDateFrom(), new Date());
		request.getSession().removeAttribute(WebKeys.TABLE_EXPERT_POSITION);
		handleList(request, response);
	}

	private void resetTableData(HttpServletRequest request) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EXPERT_POSITION);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		List<ExpertPosition> expertPositions = nomenclaturesDataProvider.getExpertPositions();
		if (expertPositions != null) {
			for (ExpertPosition c:expertPositions) {
				try {
					FlatNomenclature appStatus = c.getRelatedAppStatus();
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
