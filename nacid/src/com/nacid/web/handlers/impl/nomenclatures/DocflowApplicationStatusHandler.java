package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
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


public class DocflowApplicationStatusHandler extends NacidBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";


    public DocflowApplicationStatusHandler(ServletContext servletContext) {
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
        String name = request.getParameter("name").trim();

        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getApplicationDocflowStatus(getNumgeneratorSeriesId(request), id) == null) {
            throw new UnknownRecordException("Unknown application docflow status ID:" + id);
        }
        String groupName = getGroupName(request);
        String nomenclatureName = FlatNomenclatureHandler.getGroupNameToNomenclatureName(groupName, false);
        int nomenclatureId = FlatNomenclatureHandler.groupNameToNomenclatureIdMap.get(groupName);
        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            if (name == null || "".equals(name)) {
                webmodel.addAttribute("- грешно въведено име!");  
            }
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(id, name,request.getParameter("dateFrom"), request.getParameter("dateTo"), groupName, nomenclatureName, nomenclatureId));
        } else {
            int newId = nomenclaturesDataProvider.saveApplicationDocflowStatus(getNumgeneratorSeriesId(request), id, name, dateFrom, dateTo);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(nomenclaturesDataProvider.getApplicationDocflowStatus(getNumgeneratorSeriesId(request), newId), groupName, nomenclatureName));
        }
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS);
        request.setAttribute(WebKeys.NEXT_SCREEN, "flat_nomenclature_edit");


    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        String groupName = getGroupName(request);
        Integer nomenclatureId = FlatNomenclatureHandler.groupNameToNomenclatureIdMap.get(groupName);
        if (nomenclatureId == null) {
            throw new UnknownRecordException("Unknown nomenclature name!" + groupName);
        }
        request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(groupName, FlatNomenclatureHandler.getGroupNameToNomenclatureName(groupName, false), nomenclatureId));
        request.setAttribute(WebKeys.NEXT_SCREEN, "flat_nomenclature_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int applicationStatusId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (applicationStatusId <= 0) {
            throw new UnknownRecordException("Unknown application dkstatus ID:" + applicationStatusId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "flat_nomenclature_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationDocflowStatus applicationStatus = nomenclaturesDataProvider.getApplicationDocflowStatus(getNumgeneratorSeriesId(request), applicationStatusId);
        if (applicationStatus == null) {
            throw new UnknownRecordException("Unknown application docflow status ID:" + applicationStatusId);
        }
        String groupName = getGroupName(request);
        String nomenclatureName = FlatNomenclatureHandler.getGroupNameToNomenclatureName(groupName, false);
        request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(applicationStatus, groupName, nomenclatureName));
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        /**
         * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
         */
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
            session.setAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS, table);
            resetTableData(request);

        }
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS + WebKeys.TABLE_STATE, tableState);
        }

        TableWebModel webmodel = new TableWebModel("Статуси на заявление");
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
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int applicationStatusId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (applicationStatusId <= 0) {
            throw new UnknownRecordException("Unknown application docflow status ID:" + applicationStatusId);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationDocflowStatus applicationStatus = nomenclaturesDataProvider.getApplicationDocflowStatus(getNumgeneratorSeriesId(request), applicationStatusId);
        if (applicationStatus == null) {
            throw new UnknownRecordException("Unknown application docflow status ID:" + applicationStatusId);
        }
        nomenclaturesDataProvider.saveApplicationDocflowStatus(getNumgeneratorSeriesId(request), applicationStatus.getId(), applicationStatus.getName(), applicationStatus.getDateFrom(), new Date());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATION_DOCFLOW_STATUS);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<ApplicationDocflowStatus> applicationStatuses = nomenclaturesDataProvider.getApplicationDocflowStatuses(getNumgeneratorSeriesId(request), null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false));

        if (applicationStatuses != null) {
            for (ApplicationDocflowStatus c:applicationStatuses) {
                try {
                    table.addRow(c.getId(), c.getName(), c.getDateFrom(),  c.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }


    private static final int getNumgeneratorSeriesId(HttpServletRequest request) {
        String groupName = getGroupName(request);
        if ("nom_docflow_status".equals(groupName)) {
            return NumgeneratorDataProvider.NACID_SERIES_ID;
        } else if ("nom_regprof_docflow_status".equals(groupName)) {
            return NumgeneratorDataProvider.REGPROF_SERIES_ID;
        } else {
            throw new RuntimeException("Unknown applicationId for the given groupName..." + groupName);
        }

    }



}
