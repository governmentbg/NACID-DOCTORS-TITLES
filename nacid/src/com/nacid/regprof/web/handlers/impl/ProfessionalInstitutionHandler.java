package com.nacid.regprof.web.handlers.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.ProfessionalInstitutionImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.ProfessionalInstitutionNamesRecord;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
//RayaWritten------------------------------------------------------------------
public class ProfessionalInstitutionHandler extends RegProfBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_COUNTRY = "Държава";
    private static final String COLUMN_NAME_COUNTRY_ID = "countryId";
    private static final String COLUMN_NAME_PROFESSIONAL_INSTITUTION_TYPE_ID = "professionalInstitutionTypeId";
    private static final String COLUMN_NAME_BG_NAME = "Наименование";
    private static final String COLUMN_NAME_CITY = "Град";
    private static final String COLUMN_NAME_ADDRESS = "Адрес";
    private static final String COLUMN_NAME_PHONE = "Телефон";
    private static final String COLUMN_NAME_FAX = "Факс";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";
    private static final String COLUMN_NAME_PROFESSIONAL_INSTITUTION_TYPE = "Тип обучителна институция";
    private static final String COLUMN_NAME_ORG_NAME = "Ориг. наименование";
    private static final String FILTER_NAME_COUNTRY = "countryFilter";
    private static final String FILTER_NAME_PROF_INST_TYPE = "professionalInstitutionTypeFilter";
    
    private static final String NEXT_SCREEN = "professional_institution";
    private static final String ATTRIBUTE_NAME = "com.nacid.bl.impl.regprof.ProfessionalInstitutionImpl";
    
    private static final int MAX_NAMES_COUNT = 20;
    
    public ProfessionalInstitutionHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(ATTRIBUTE_NAME, new ProfessionalInstitutionImpl());
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        generateCountryComboBox(null, nomDP, request);
        generateProfInstTypeComboBox(null, nomDP, request);
        request.setAttribute("listSize", 0);
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {

        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
            throw new UnknownRecordException("Unknown professional institution ID:" + id);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        ProfessionalInstitutionDataProvider dp = nacidDataProvider.getProfessionalInstitutionDataProvider();
        ProfessionalInstitution record = dp.getProfessionalInstitution(id);
        initFormerNames(request, record, dp);
        request.setAttribute(ATTRIBUTE_NAME, record);
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        generateCountryComboBox(record.getCountryId(), nomDP, request);
        generateProfInstTypeComboBox(record.getProfessionalInstitutionTypeId(), nomDP, request);
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            //table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_COUNTRY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_PROFESSIONAL_INSTITUTION_TYPE_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            //table.addColumnHeader(COLUMN_NAME_ORG_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_BG_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_CITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_ADDRESS, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_PHONE, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_FAX, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PROFESSIONAL_INSTITUTION_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
             
            session.setAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION, table);
            resetTableData(request);
        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_COUNTRY, COLUMN_NAME_COUNTRY_ID, request, table, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_PROF_INST_TYPE, COLUMN_NAME_PROFESSIONAL_INSTITUTION_TYPE_ID, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на " + MessagesBundle.getMessagesBundle().getValue("professional_institution"));
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideUnhideColumn(COLUMN_NAME_COUNTRY_ID, true);
        webmodel.hideUnhideColumn(COLUMN_NAME_PROFESSIONAL_INSTITUTION_TYPE_ID, true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "professionalinstitution_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
        .getAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();            
            FilterWebModel profInstTypeFilter = generateProfInstTypeWebModel(DataConverter.parseInt(request.getParameter(FILTER_NAME_PROF_INST_TYPE), 0), getNacidDataProvider().getNomenclaturesDataProvider(), request);
            profInstTypeFilter.setLabelOnTop(true);
            filtersWebModel.addFiler(profInstTypeFilter);
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        ProfessionalInstitution record = (ProfessionalInstitutionImpl) request.getAttribute(ATTRIBUTE_NAME);
        if (record == null) {
            throw new UnknownRecordException("Unknown professional institution...");
        }
        ProfessionalInstitutionDataProvider dp = getNacidDataProvider().getProfessionalInstitutionDataProvider();
        if (record.getId() != null) {
            //dp.deleteProfessionalInstitutionNames(record.getId());
        }
        
        record = dp.saveProfessionalInstitutionRecord(record);
        
        List<String> listOfNames = new ArrayList<String>();
        List<ProfessionalInstitutionNamesRecord> formerNames = dp.getProfessionalInstitutionNames(record.getId(), null, false);
        List<String> existingFormerNames = new ArrayList<String>();
        for (ProfessionalInstitutionNamesRecord formerName : formerNames) {
            existingFormerNames.add(formerName.getFormerName().toLowerCase());
        }

        int namesCount = DataConverter.parseInteger(request.getParameter("list_size"), -1);
        if (namesCount > 0) {
            if (namesCount > MAX_NAMES_COUNT) {
                namesCount = MAX_NAMES_COUNT;
            }
            for (int i = 0; i < namesCount; i++) {
                String formerName = DataConverter.parseString(request.getParameter("former_name" + i), "");
                formerName = formerName.trim();
                if (!formerName.isEmpty() && !listOfNames.contains(formerName.toLowerCase())) {
                    listOfNames.add(formerName.toLowerCase());
                    if (!existingFormerNames.contains(formerName.toLowerCase())) {
                        ProfessionalInstitutionNamesRecord nameRecord = new ProfessionalInstitutionNamesRecord(null, record.getId(), formerName, 1);
                        dp.saveProfessionalInstitutionName(nameRecord);
                    }
                }
            }
        }
        
        String singleFormerName = DataConverter.parseString(request.getParameter("former_name"), "");
        singleFormerName = singleFormerName.trim();
        if (!singleFormerName.isEmpty() && !listOfNames.contains(singleFormerName.toLowerCase()) && !existingFormerNames.contains(singleFormerName.toLowerCase())) {
            listOfNames.add(singleFormerName.toLowerCase());
            ProfessionalInstitutionNamesRecord nameRecord = new ProfessionalInstitutionNamesRecord(null, record.getId(), singleFormerName, 1);
            dp.saveProfessionalInstitutionName(nameRecord);
        }
        
        for (ProfessionalInstitutionNamesRecord formerNameRecord : formerNames) {
            if (!listOfNames.contains(formerNameRecord.getFormerName().toLowerCase())) {
                dp.disableProfessionalInstitutionName(formerNameRecord);
            }
        }
        
        request.setAttribute(ATTRIBUTE_NAME, record);
        request.getSession().removeAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION);
        resetTableData(request);
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
        
        SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, systemMessageWebmodel);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        generateCountryComboBox(record.getCountryId(), nomDP, request);
        generateProfInstTypeComboBox(record.getProfessionalInstitutionTypeId(), nomDP, request);
        initFormerNames(request, record, dp);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
            throw new UnknownRecordException("Unknown id:" + id);
        }
        ProfessionalInstitutionDataProvider piDP = getNacidDataProvider().getProfessionalInstitutionDataProvider();
        piDP.disableProfessionalInstitution(id);
        request.getSession().removeAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION);
        handleList(request, response);
    }
    
    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_PROFESSIONAL_INSTITUTION);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ProfessionalInstitutionDataProvider dp = nacidDataProvider.getProfessionalInstitutionDataProvider();
        List<ProfessionalInstitutionImpl> profInstitutions = dp.getProfessionalInstitutions();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        if (profInstitutions != null) {
            for (ProfessionalInstitution profIn : profInstitutions) {
                Country country = nomDP.getCountry(profIn.getCountryId());
                String countryName = "";
                if(country != null) {
                    countryName = country.getName();
                }
                FlatNomenclature professionalInstitutionType = nomDP.getFlatNomenclature(
                        NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE, profIn.getProfessionalInstitutionTypeId());
                String profInsType = "";
                if(professionalInstitutionType != null){
                    profInsType = professionalInstitutionType.getName();
                }
                try {
                    table.addRow(profIn.getId(), profIn.getProfessionalInstitutionTypeId(), 
                           /*profIn.getOrgName(),*/ profIn.getBgName(), profInsType, profIn.getDateFrom(), profIn.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }

            }
        }
    }
    
    private static ComboBoxWebModel generateCountryComboBox(Integer activeCountryId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
        ComboBoxWebModel model = ComboBoxUtils.generateNomenclaturesComboBox(activeCountryId, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, nomDP, false,
                request, "countryCombo", OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        request.setAttribute("countryId", model);
        return model;
    }
    
    private static ComboBoxWebModel generateProfInstTypeComboBox(Integer profInsTypeId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
        ComboBoxWebModel model = ComboBoxUtils.generateNomenclaturesComboBox(profInsTypeId, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE, nomDP, false, 
                request, "profInstTypeCombo", OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        request.setAttribute("professionalInstitutionTypeId", model);
        return model;
        
    }
    
    private static FilterWebModel generateProfInstTypeWebModel(Integer profInstTypeId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
        ComboBoxWebModel combobox = generateProfInstTypeComboBox(profInstTypeId, nomDP, request);
        ComboBoxFilterWebModel result = new ComboBoxFilterWebModel(combobox, FILTER_NAME_PROF_INST_TYPE, COLUMN_NAME_PROFESSIONAL_INSTITUTION_TYPE);
        result.setElementClass("brd w300");
        return result;
    }
    
    private static void initFormerNames(HttpServletRequest request, ProfessionalInstitution institution, ProfessionalInstitutionDataProvider dp) {
        List<ProfessionalInstitutionNamesRecord> formerNames = null;
        if (institution != null) {
            formerNames = dp.getProfessionalInstitutionNames(institution.getId(), null, false);
            if (formerNames != null && formerNames.size() > 0) {
                request.setAttribute("formerNames", formerNames);
            }
        }
        request.setAttribute("listSize", formerNames == null ? 0 : formerNames.size());
    }
}
//---------------------------------------------------------------------------------