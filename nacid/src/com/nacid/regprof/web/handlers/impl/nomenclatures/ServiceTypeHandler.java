package com.nacid.regprof.web.handlers.impl.nomenclatures;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.converters.BigDecimalConverter;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleDirective;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
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
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

//RayaWritten------------------------------------
public class ServiceTypeHandler extends RegProfBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_EXECUTION_DAYS = "Брой календарни дни";
    private static final String COLUMN_NAME_SERVICE_PRICE= "Цена на услугата"; 
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";

    public ServiceTypeHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "service_type_edit");
    }
    
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int serviceTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (serviceTypeId <= 0) {
            throw new UnknownRecordException("Unknown Service Type ID:" + serviceTypeId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "service_type_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ServiceType serviceType = nomenclaturesDataProvider.getServiceType(serviceTypeId);
        if (serviceType == null) {
            throw new UnknownRecordException("Unknown Service Type ID:" + serviceTypeId);
        }
        request.setAttribute(WebKeys.SERVICE_TYPE, serviceType);
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
        Integer executionDays = DataConverter.parseInteger(request.getParameter("executionDays"), null);
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        BigDecimal servicePrice = DataConverter.parseBigDecimal(request.getParameter("price").replace(",", "."), new BigDecimal(0));
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getServiceType(id) == null) {
            throw new UnknownRecordException("Unknown ServiceType ID:" + id);
        }
        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            webmodel.addAttribute("- грешно въведено име!");
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);         
        } else {
            int newId = nomenclaturesDataProvider.saveServiceType(id, name, executionDays, dateFrom, dateTo, servicePrice);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            ServiceType surviceType = nomenclaturesDataProvider.getServiceType(newId);
            if(surviceType == null){
                throw new UnknownRecordException("Unknown Service Type ID:" + newId);
            }
            request.setAttribute(WebKeys.SERVICE_TYPE, surviceType);
        }
        
        request.getSession().removeAttribute(WebKeys.TABLE_SERVICE_TYPE);
        request.setAttribute(WebKeys.NEXT_SCREEN, "service_type_edit");
    }
    
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int serviceTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (serviceTypeId <= 0) {
            throw new UnknownRecordException("Unknown Service Type ID:" + serviceTypeId);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ServiceType serviceType = nomenclaturesDataProvider.getServiceType(serviceTypeId);
        if (serviceType == null) {
            throw new UnknownRecordException("Unknown Service Type ID:" + serviceTypeId);
        }
        nomenclaturesDataProvider.saveServiceType(serviceType.getId(), serviceType.getName(), serviceType.getExecutionDays(), 
                serviceType.getDateFrom(), new Date(), serviceType.getServicePrice());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_SERVICE_TYPE);
        handleList(request, response);
    }
    
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_SERVICE_TYPE);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        /**
         * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
         */
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_EXECUTION_DAYS, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_SERVICE_PRICE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
            session.setAttribute(WebKeys.TABLE_SERVICE_TYPE, table);
            resetTableData(request);

        }
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_SERVICE_TYPE + WebKeys.TABLE_STATE);
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_SERVICE_TYPE + WebKeys.TABLE_STATE, tableState);
        }

        TableWebModel webmodel = new TableWebModel("Типове услуги");
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");
      //Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_SERVICE_TYPE + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_SERVICE_TYPE + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }
    
    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_SERVICE_TYPE);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<ServiceType> serviceTypes = nomenclaturesDataProvider.getServiceTypes(null, 
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false), null);
        if (serviceTypes != null) {
            for (ServiceType st :serviceTypes) {
                try {
                    table.addRow(st.getId(), st.getName(), st.getExecutionDays(), st.getServicePrice()!= null ? st.getServicePrice().toPlainString():"",
                             st.getDateFrom(), st.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
//------------------------------------------------------
