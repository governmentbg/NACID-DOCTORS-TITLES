package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.applications.CompetentInstitutionDataProvider;
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
import com.nacid.web.model.applications.CompetentInstitutionWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

public class CompetentInstitutionHandler extends NacidBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_COUNTRY_ID = "countryId";
    private static final String COLUMN_NAME_COUNTRY = "Държава";
    private static final String COLUMN_NAME_BG_NAME = "Наименование";
    private static final String COLUMN_NAME_ORG_NAME = "Оригинално наименование";
    private static final String COLUMN_NAME_ADDRESS = "Адрес";
    //private static final String COLUMN_NAME_PHONE = "Телефон";
    //private static final String COLUMN_NAME_FAX = "Факс";
    //private static final String COLUMN_NAME_EMAIL = "Електронна поща";

    private static final String FILTER_NAME_COUNTRY = "countryFilter";
    public CompetentInstitutionHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY,
                nDP, true, request, WebKeys.COMBO, null, true);
        request.setAttribute(WebKeys.NEXT_SCREEN, "competent_institution_edit");
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {

        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        CompetentInstitution competentInstitution = getCompetentInstitution(request, nacidDataProvider);

        request.setAttribute(WebKeys.NEXT_SCREEN, "competent_institution_edit");
        ComboBoxUtils.generateNomenclaturesComboBox(competentInstitution.getCountryId(), NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, nomDP, true,
                request, WebKeys.COMBO, null, true);
        request.setAttribute(WebKeys.COMPETENT_INSTITUTION_WEB_MODEL, new CompetentInstitutionWebModel(competentInstitution));

    }

    public static CompetentInstitution getCompetentInstitution(HttpServletRequest request, NacidDataProvider nacidDataProvider) {
        int competentInstitutionId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (competentInstitutionId <= 0) {
            throw new UnknownRecordException("Unknown competent institution ID:" + competentInstitutionId);
        }
        CompetentInstitutionDataProvider competentInstitutionDataProvider = nacidDataProvider.getCompetentInstitutionDataProvider();
        CompetentInstitution competentInstitution = competentInstitutionDataProvider.getCompetentInstitution(competentInstitutionId);
        if (competentInstitution == null) {
            throw new UnknownRecordException("Unknown competent institution ID:" + competentInstitutionId);
        }
        return competentInstitution;
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COUNTRY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_BG_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_ORG_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_ADDRESS, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_PHONE, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_FAX, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);

            session.setAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION + WebKeys.TABLE_STATE, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_COUNTRY, COLUMN_NAME_COUNTRY_ID, request, table, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на компетентни иснтитуции");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideUnhideColumn(COLUMN_NAME_COUNTRY_ID, true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            
            filtersWebModel.addFiler(UniversityHandler.generateCountryFilterWebModel(DataConverter.parseInteger(request.getParameter(FILTER_NAME_COUNTRY), null), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            
            session.setAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }

        CompetentInstitutionDataProvider competentInstitutionDataProvider = getNacidDataProvider().getCompetentInstitutionDataProvider();

        CompetentInstitution newCompetentInstitution = saveCompetentInstitution(request, getNacidDataProvider());

        request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        resetTableData(request);

        request.setAttribute(WebKeys.COMPETENT_INSTITUTION_WEB_MODEL, new CompetentInstitutionWebModel(competentInstitutionDataProvider.getCompetentInstitution(newCompetentInstitution.getId())));

        ComboBoxUtils.generateNomenclaturesComboBox(newCompetentInstitution.getCountryId(), NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider()
                .getNomenclaturesDataProvider(), true, request, WebKeys.COMBO, null, false);

        request.setAttribute(WebKeys.NEXT_SCREEN, "competent_institution_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION);

    }


    public static CompetentInstitution saveCompetentInstitution(HttpServletRequest request, NacidDataProvider nacidDataProvider) {
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        int countryId = DataConverter.parseInt(request.getParameter("country"), 0);
        String name = request.getParameter("name");
        String originalName = request.getParameter("original_name");
        String addrDetails = request.getParameter("address_details");
        String phone = request.getParameter("phone");
        String fax = request.getParameter("fax");
        String email = request.getParameter("email");
        String url = DataConverter.convertUrl(request.getParameter("url"));
        String notes = request.getParameter("notes");

        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));

        if(id == 0 && dateFrom == null) {
            dateFrom = new Date();
        }

        CompetentInstitutionDataProvider competentInstitutionDataProvider = nacidDataProvider.getCompetentInstitutionDataProvider();

        if (id != 0 && competentInstitutionDataProvider.getCompetentInstitution(id) == null) {
            throw new UnknownRecordException("Unknown competent institution ID:" + id);
        }

        int newId = competentInstitutionDataProvider.saveCompetentInstitution(id, countryId, name, originalName, phone, fax, email, addrDetails, url,
                notes, dateFrom, dateTo);

        return competentInstitutionDataProvider.getCompetentInstitution(newId);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int institutionId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (institutionId <= 0) {
            throw new UnknownRecordException("Unknown competent institution Id:" + institutionId);
        }
        CompetentInstitutionDataProvider competentInstitutionDataProvider = getNacidDataProvider().getCompetentInstitutionDataProvider();
        competentInstitutionDataProvider.deleteCompetentInstitution(institutionId);

        request.getSession().removeAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_COMPETENT_INSTITUTION);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        CompetentInstitutionDataProvider competentInstitutionDataProvider = nacidDataProvider.getCompetentInstitutionDataProvider();
        List<CompetentInstitution> competentInstitutions = competentInstitutionDataProvider.getCompetentInstitutions((Integer)null, false);

        if (competentInstitutions != null) {
            for (CompetentInstitution i : competentInstitutions) {
                try {
                    Country c = i.getCountry();
                    table.addRow(i.getId(), c == null ? "" : c.getName(), c == null ? null : c.getId(), i.getName(), i.getOriginalName(), i.getAddressDetails()/*, i.getPhone(), i
                            .getFax(), i.getEmail()*/);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

}
