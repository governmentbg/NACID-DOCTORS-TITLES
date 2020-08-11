package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationStatus;
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
import com.nacid.web.model.nomenclatures.ApplicationStatusWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


public class ApplicationStatusHandler extends NacidBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";
    private static final String COLUMN_NAME_LEGAL_STATUS = "Правен статус";


    public ApplicationStatusHandler(ServletContext servletContext) {
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
        if (id != 0 && nomenclaturesDataProvider.getApplicationStatus(getNumgeneratorEntryNumId(request), id) == null) {
            throw new UnknownRecordException("Unknown application status ID:" + id);
        }
        boolean isLegal = DataConverter.parseBoolean(request.getParameter("is_legal"));

        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            if (name == null || "".equals(name)) {
                webmodel.addAttribute("- грешно въведено име!");  
            }
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.APPLICATION_STATUS, new ApplicationStatusWebModel(id + "", name,request.getParameter("dateFrom"), request.getParameter("dateTo"), isLegal));
        } else {
            int newId = nomenclaturesDataProvider.saveApplicationStatus(getNumgeneratorEntryNumId(request), id, name, dateFrom, dateTo, isLegal);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            request.setAttribute(WebKeys.APPLICATION_STATUS, new ApplicationStatusWebModel(nomenclaturesDataProvider.getApplicationStatus(getNumgeneratorEntryNumId(request), newId)));
        }
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_STATUS);
        request.setAttribute(WebKeys.NEXT_SCREEN, "application_status_edit");


    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "application_status_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int applicationStatusId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (applicationStatusId <= 0) {
            throw new UnknownRecordException("Unknown application status ID:" + applicationStatusId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "application_status_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationStatus applicationStatus = nomenclaturesDataProvider.getApplicationStatus(getNumgeneratorEntryNumId(request), applicationStatusId);
        if (applicationStatus == null) {
            throw new UnknownRecordException("Unknown application status ID:" + applicationStatusId);
        }
        request.setAttribute(WebKeys.APPLICATION_STATUS, new ApplicationStatusWebModel(applicationStatus));
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPLICATION_STATUS);
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
            table.addColumnHeader(COLUMN_NAME_LEGAL_STATUS, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            session.setAttribute(WebKeys.TABLE_APPLICATION_STATUS, table);
            resetTableData(request);

        }
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_APPLICATION_STATUS + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_APPLICATION_STATUS + WebKeys.TABLE_STATE, tableState);
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
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_APPLICATION_STATUS + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_APPLICATION_STATUS + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int applicationStatusId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (applicationStatusId <= 0) {
            throw new UnknownRecordException("Unknown application status ID:" + applicationStatusId);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationStatus applicationStatus = nomenclaturesDataProvider.getApplicationStatus(getNumgeneratorEntryNumId(request), applicationStatusId);
        if (applicationStatus == null) {
            throw new UnknownRecordException("Unknown application status ID:" + applicationStatusId);
        }
        nomenclaturesDataProvider.saveApplicationStatus(getNumgeneratorEntryNumId(request), applicationStatus.getId(), applicationStatus.getName(), applicationStatus.getDateFrom(), new Date(), applicationStatus.isLegal());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_STATUS);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATION_STATUS);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<ApplicationStatus> applicationStatuses = nomenclaturesDataProvider.getApplicationStatuses(getNumgeneratorEntryNumId(request), null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false), false);

        if (applicationStatuses != null) {
            for (ApplicationStatus c:applicationStatuses) {
                try {
                    table.addRow(c.getId(), c.getName(), c.getDateFrom(),  c.getDateTo(), c.isLegal());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }


    private static final int getNumgeneratorEntryNumId(HttpServletRequest request) {
        String groupName = getGroupName(request);
        if ("nom_appstatus".equals(groupName)) {
            return NumgeneratorDataProvider.NACID_SERIES_ID;
        } else if ("nom_regprofappstatus".equals(groupName)) {
            return NumgeneratorDataProvider.REGPROF_SERIES_ID;
        } else {
            throw new RuntimeException("Unknown applicationId for the given groupName..." + groupName);
        }

    }



}
