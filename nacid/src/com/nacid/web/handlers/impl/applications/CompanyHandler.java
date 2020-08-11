package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Company;
import com.nacid.bl.applications.CompanyDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
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
import com.nacid.web.model.applications.CompanyWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

public class CompanyHandler extends NacidBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Име";
    private static final String COLUMN_NAME_PHONE = "Телефон";
    private static final String COLUMN_NAME_DATEFROM = "от дата";
    private static final String COLUMN_NAME_DATETO = "до дата";
    
    private static final String EDIT_SCREEN = "company_edit";
    

    public CompanyHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_COMPANY);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PHONE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATEFROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATETO, CellValueDef.CELL_VALUE_TYPE_DATE);


            session.setAttribute(WebKeys.TABLE_COMPANY, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_COMPANY + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            //TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_COUNTRY, COLUMN_NAME_COUNTRY, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_DIPLOMA_TYPE + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на фирмите");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(
                WebKeys.TABLE_COMPANY + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            //filtersWebModel.addFiler(generateCountryFilterComboBox(request.getParameter(FILTER_NAME_COUNTRY), getNacidDataProvider()
            //        .getNomenclaturesDataProvider(), request));
            session.setAttribute(WebKeys.TABLE_COMPANY + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.COMPANY_WEB_MODEL, new CompanyWebModel(null));
        
        List<Country> countries = getNacidDataProvider().getNomenclaturesDataProvider()
                .getCountries(null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, 
                countries, true, request, "companyCountry", false);
        
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        
        int companyId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (companyId <= 0) {
            throw new UnknownRecordException("Unknown company ID:" + companyId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        
        Company company = getNacidDataProvider()
            .getCompanyDataProvider().getCompany(companyId);
        
        if (company == null) {
            throw new UnknownRecordException("Unknown company ID:" + companyId);
        }

        List<Country> countries = getNacidDataProvider().getNomenclaturesDataProvider().getCountries(null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(
                company.getCountryId(), countries, true, request, "companyCountry", false);
        
        request.setAttribute(WebKeys.COMPANY_WEB_MODEL, 
               new CompanyWebModel(company));

    }
    
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext())
                    .processRequest(request, response);
            return;
        }
        
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        String name = request.getParameter("companyName");
        String eik = request.getParameter("companyEik");
        int countryId = DataConverter.parseInt(request.getParameter("countryId"), 0);
        String city = request.getParameter("city");
        String pcode = request.getParameter("pcode");
        String addressDetails = request.getParameter("addressDetails");
        String phone = request.getParameter("phone");
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        
        
        CompanyDataProvider compDP = getNacidDataProvider().getCompanyDataProvider();
        
        if (id != 0 && compDP.getCompany(id) == null) {
            throw new UnknownRecordException("Unknown company ID:" + id);
        }
        
        
        
        int newId = compDP.saveCompany(id, name, countryId, city, pcode, 
                addressDetails, phone, dateFrom, dateTo, eik, null);

        request.setAttribute(WebKeys.SYSTEM_MESSAGE,
                new SystemMessageWebModel("Данните бяха въведени в базата",
                        SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        resetTableData(request);
        request.setAttribute(WebKeys.COMPANY_WEB_MODEL, new CompanyWebModel(
                compDP.getCompany(newId)));

        List<Country> countries = getNacidDataProvider().getNomenclaturesDataProvider().getCountries(null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(
                countryId, countries, true, request, "companyCountry", false);
        
        
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        request.getSession().removeAttribute(WebKeys.TABLE_DIPLOMA_TYPE);
        
    }
    
    @Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int companyId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (companyId <= 0) {
          throw new UnknownRecordException("Unknown company Id:" + companyId);
        }
        
        CompanyDataProvider compDP = getNacidDataProvider().getCompanyDataProvider();
        compDP.disableCompany(companyId);
        
        request.getSession().removeAttribute(WebKeys.TABLE_COMPANY);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_COMPANY);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        CompanyDataProvider compDP = nacidDataProvider.getCompanyDataProvider();
        
        List<Company> companies = compDP.getCompanies(false);

        if (companies != null) {
            for (Company comp : companies) {

                try {
                    table.addRow(comp.getId(), comp.getName(), comp.getPhone(),
                            comp.getDateFrom(), comp.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
