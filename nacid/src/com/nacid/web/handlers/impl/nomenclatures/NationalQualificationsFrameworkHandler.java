package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.NationalQualificationsFrameworkWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


public class NationalQualificationsFrameworkHandler extends NacidBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_COUNTRY_ID = "countryId";
    private static final String COLUMN_NAME_COUNTRY = "Държава";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";

    private static final String FILTER_COUNTRY = "filterCountry";


    public NationalQualificationsFrameworkHandler(ServletContext servletContext) {
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
        int countryId = DataConverter.parseInt(request.getParameter("country_id"), -1);

        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getNationalQualificationsFramework(id) == null) {
            throw new UnknownRecordException("Unknown NQF ID:" + id);
        }
        if (name == null || "".equals(name) || countryId == -1 ) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            if (name == null || "".equals(name)) {
                webmodel.addAttribute("- грешно въведено име!");  
            }
            if (countryId == -1) {
                webmodel.addAttribute("- грешно въведена държава!");
            }
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.NATIONAL_QUALIFICATIONS_FRAMEWORK, new NationalQualificationsFrameworkWebModel(id, name, request.getParameter("dateFrom"), request.getParameter("dateTo"), countryId));
        } else {
            int newId = nomenclaturesDataProvider.saveNationalQualificationsFramework(id, name, countryId, dateFrom, dateTo);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            request.setAttribute(WebKeys.NATIONAL_QUALIFICATIONS_FRAMEWORK, new NationalQualificationsFrameworkWebModel(nomenclaturesDataProvider.getNationalQualificationsFramework(newId)));
        }

        fillComboboxWebModels(countryId, request);
        request.getSession().removeAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK);
        request.setAttribute(WebKeys.NEXT_SCREEN, "national_qualifications_framework_edit");


    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "national_qualifications_framework_edit");
        fillComboboxWebModels(null, request);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int nqfId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (nqfId <= 0) {
            throw new UnknownRecordException("Unknown NQF ID:" + nqfId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "national_qualifications_framework_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        NationalQualificationsFramework nationalQualificationsFramework = nomenclaturesDataProvider.getNationalQualificationsFramework(nqfId);
        if (nationalQualificationsFramework == null) {
            throw new UnknownRecordException("Unknown NQF ID:" + nqfId);
        }

        fillComboboxWebModels(nationalQualificationsFramework.getCountryId(),  request);
        request.setAttribute(WebKeys.NATIONAL_QUALIFICATIONS_FRAMEWORK, new NationalQualificationsFrameworkWebModel(nationalQualificationsFramework));
    }

    private void fillComboboxWebModels(Integer countryId, HttpServletRequest request) {
        NomenclaturesDataProvider ndp = getNacidDataProvider().getNomenclaturesDataProvider();
        ComboBoxUtils.generateNomenclaturesComboBox(countryId, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, ndp, false, request, "countries", null, true);
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        /**
         * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
         */
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COUNTRY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
            session.setAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK, table);
            resetTableData(request);

        }
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_COUNTRY, COLUMN_NAME_COUNTRY_ID, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK + WebKeys.TABLE_STATE, tableState);
        }

        TableWebModel webmodel = new TableWebModel("Национална квалификационна рамка");
        webmodel.hideUnhideColumn(COLUMN_NAME_COUNTRY_ID, true);

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        if (DataConverter.parseBoolean(request.getParameter("inner"))) {
            request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");
        } else {
            request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");	
        }


        //Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            filtersWebModel.addFiler(generateCountryComboBox(DataConverter.parseInteger(request.getParameter(FILTER_COUNTRY), null), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            session.setAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }





    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int nqfId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (nqfId <= 0) {
            throw new UnknownRecordException("Unknown National Qualifications Framework ID:" + nqfId);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        NationalQualificationsFramework nqf = nomenclaturesDataProvider.getNationalQualificationsFramework(nqfId);
        if (nqf == null) {
            throw new UnknownRecordException("Unknown National Qualifications Framework ID:" + nqfId);
        }
        nomenclaturesDataProvider.saveNationalQualificationsFramework(nqf.getId(), nqf.getName(), nqf.getCountryId(), nqf.getDateFrom(), new Date());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_NATIONAL_QUALIFICATIONS_FRAMEWORK);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<NationalQualificationsFramework> educationLevels = nomenclaturesDataProvider.getNationalQualificationsFrameworks(null, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, false));

        if (educationLevels != null) {
            for (NationalQualificationsFramework oel:educationLevels) {
                try {
                    Country c = nomenclaturesDataProvider.getCountry(oel.getCountryId());
                    table.addRow(oel.getId(), oel.getName(), oel.getCountryId(), c.getName(), oel.getDateFrom(),  oel.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }


    private static ComboBoxFilterWebModel generateCountryComboBox(Integer activeCountryId, NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryId == null ? null : activeCountryId + "", true);
        List<Country> countries = nomenclaturesDataProvider.getCountries(null, null);
        if (countries != null) {
            for (FlatNomenclature ea:countries) {
                combobox.addItem(ea.getId() + "", ea.getName() + (ea.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_COUNTRY, "Държава:");
        res.setElementClass("brd");
        return res;
    }
}
