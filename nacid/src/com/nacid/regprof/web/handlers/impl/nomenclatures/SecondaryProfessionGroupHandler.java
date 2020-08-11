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
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionGroup;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.nomenclatures.FlatNomenclatureHandler;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.regprof.SecondaryProfessionGroupWebModel;
import com.nacid.web.model.nomenclatures.regprof.SecondaryProfessionalQualificationWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
//RayaWritten----------------------------------------------------------------------
public class SecondaryProfessionGroupHandler extends RegProfBaseRequestHandler{
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_CODE = "Код";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";

    private static final String FILTER_NAME_PROF_GROUP_ID = "profGroupFilter";

    public SecondaryProfessionGroupHandler(ServletContext servletContext) {
        super(servletContext);
    } 
    
    public void handleList(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_CODE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);

            session.setAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP, table);
            resetTableData(request);

        }

        //TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP + WebKeys.TABLE_STATE, tableState);
        }

        //TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Професионални направления - обучение");
        //webmodel.setColumnFormatter("userDate", CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        //Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response){
        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
            throw new UnknownRecordException("Unknown Secondary Profession Group ID:" + id);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_prof_group_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        SecondaryProfessionGroup spg = nomenclaturesDataProvider.getSecondaryProfessionGroup(id);


        if (spg == null) {
            throw new UnknownRecordException("Unknown SecondaryProfessionGroup ID:" + id);
        }
        
        request.setAttribute(WebKeys.SECONDARY_PROFESSIONAL_GROUP, new SecondaryProfessionGroupWebModel(spg));
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_prof_group_edit");
    } 

    public void handleSave(HttpServletRequest request, HttpServletResponse response){
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getSecondaryProfessionGroup(id) == null) {
            throw new UnknownRecordException("Unknown SecondaryProfessionGroup ID:" + id);
        }
        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            webmodel.addAttribute("- грешно въведено име!");
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.SECONDARY_PROFESSIONAL_GROUP, new SecondaryProfessionGroupWebModel(id, name, request.getParameter("dateFrom"), request.getParameter("dateTo"), code));
        } else {
            int newId = nomenclaturesDataProvider.saveSecondaryProfessionGroup(id, name, dateFrom, dateTo, code);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            request.setAttribute(WebKeys.SECONDARY_PROFESSIONAL_GROUP, new SecondaryProfessionGroupWebModel(nomenclaturesDataProvider.getSecondaryProfessionGroup(newId)));
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_prof_group_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response){
        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
          throw new UnknownRecordException("Unknown SecondaryProfessionGroup ID:" + id);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        SecondaryProfessionGroup secondaryProfessionGroup = nomenclaturesDataProvider.getSecondaryProfessionGroup(id);
        if (secondaryProfessionGroup == null) {
          throw new UnknownRecordException("Unknown SecondaryProfessionGroupn ID:" + id);
        }
        nomenclaturesDataProvider.saveSecondaryProfessionGroup(secondaryProfessionGroup.getId(), secondaryProfessionGroup.getName(),
                secondaryProfessionGroup.getDateFrom(), Utils.getToday(),
                secondaryProfessionGroup.getCode());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_SECONDARY_PROFESSION_GROUP);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<SecondaryProfessionGroup> groups = nomenclaturesDataProvider.
        getSecondaryProfessionGroups(null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false));

        if (groups != null) {
            for (SecondaryProfessionGroup q:groups) {
                try {
                    table.addRow(q.getId(), q.getCode(), q.getName(), q.getDateFrom(),  q.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
//---------------------------------------------------------------------------------
