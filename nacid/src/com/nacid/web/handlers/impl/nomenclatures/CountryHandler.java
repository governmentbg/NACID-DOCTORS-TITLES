package com.nacid.web.handlers.impl.nomenclatures;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.CountryWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;


public class CountryHandler extends NacidBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_ISO3166_CODE = "ISO 3166 Код";
    private static final String COLUMN_NAME_OFFICIAL_NAME = "Официално наименование";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";


    public CountryHandler(ServletContext servletContext) {
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
        String iso3166Code = request.getParameter("iso3166Code");
        String officialName = request.getParameter("officialName").trim();
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getCountry(id) == null) {
            throw new UnknownRecordException("Unknown Country ID:" + id);
        }
        if (name == null || "".equals(name) || iso3166Code == null || "".equals(iso3166Code)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            if (name == null || "".equals(name)) {
                webmodel.addAttribute("- грешно въведено име!");  
            }
            if (iso3166Code == null || "".equals(iso3166Code)) {
                webmodel.addAttribute("- грешно въведен iso3166 код!");  
            }
            if (officialName == null || "".equals(officialName)) {
                webmodel.addAttribute("- грешно въведено официално име!");
            }
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.COUNTRY, new CountryWebModel(id, name, iso3166Code, officialName, request.getParameter("dateFrom"), request.getParameter("dateTo")));
        } else {
            int newId = nomenclaturesDataProvider.saveCountry(id, name, iso3166Code, officialName, dateFrom, dateTo);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            request.setAttribute(WebKeys.COUNTRY, new CountryWebModel(nomenclaturesDataProvider.getCountry(newId)));
        }
        request.getSession().removeAttribute(WebKeys.TABLE_COUNTRY);
        request.setAttribute(WebKeys.NEXT_SCREEN, "country_edit");


    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "country_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int countryId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (countryId <= 0) {
            throw new UnknownRecordException("Unknown Country ID:" + countryId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "country_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        Country country = nomenclaturesDataProvider.getCountry(countryId);
        if (country == null) {
            throw new UnknownRecordException("Unknown Country ID:" + countryId);
        }
        request.setAttribute(WebKeys.COUNTRY, new CountryWebModel(country));
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_COUNTRY);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        /**
         * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
         */
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_ISO3166_CODE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_OFFICIAL_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
            session.setAttribute(WebKeys.TABLE_COUNTRY, table);
            resetTableData(request);

        }
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_COUNTRY + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_COUNTRY + WebKeys.TABLE_STATE, tableState);
        }

        TableWebModel webmodel = new TableWebModel("Държави");
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
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_COUNTRY + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_COUNTRY + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int countryId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (countryId <= 0) {
            throw new UnknownRecordException("Unknown Country ID:" + countryId);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        Country country = nomenclaturesDataProvider.getCountry(countryId);
        if (country == null) {
            throw new UnknownRecordException("Unknown Country ID:" + countryId);
        }
        nomenclaturesDataProvider.saveCountry(country.getId(), country.getName(), country.getIso3166Code(), country.getOfficialName(), country.getDateFrom(), new Date());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_COUNTRY);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_COUNTRY);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<Country> countries = nomenclaturesDataProvider.getCountries(null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false));

        if (countries != null) {
            for (Country c:countries) {
                try {
                    table.addRow(c.getId(), c.getName(), c.getIso3166Code(), c.getOfficialName(), c.getDateFrom(),  c.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
