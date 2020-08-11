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
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.nomenclatures.FlatNomenclatureHandler;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.regprof.SecondarySpecialityWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class SecondarySpecialityHandler extends RegProfBaseRequestHandler {
    
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_CODE = "Код";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_PROFESSIONAL_QUALIFICATION_NAME = "Проф. квалификация";
    private static final String COLUMN_NAME_PROFESSIONAL_QUALIFICATION_ID = "Проф. квалификация id";
    private static final String COLUMN_NAME_QUALIFICATION_DEGREE_NAME = "Степен на проф. квалификация";
    private static final String COLUMN_NAME_QUALIFICATION_DEGREE_ID = "Степен на проф. квалификация id";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";
    
    private static final String FILTER_NAME_PROF_QUALIFICATION = "professionalQualificationFilter";
    private static final String FILTER_NAME_QUALIFICATION_DEGREE = "qualificationDegreeFilter";
    
    //private static final String attributeName = "com.nacid.bl.impl.nomenclatures.regprof.SecondarySpecialityImpl";

    public SecondarySpecialityHandler(ServletContext servletContext) {
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
        Integer profQualificationId = DataConverter.parseInteger(request.getParameter("prof_qualification_id"), -1);
        Integer qualificationDegreeId = DataConverter.parseInteger(request.getParameter("qualification_degree_id"), -1);
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        String code = request.getParameter("code");
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getSecondarySpeciality(id) == null) {
            throw new UnknownRecordException("Unknown Qualification ID:" + id);
        }
        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            if (name == null || "".equals(name)) {
                webmodel.addAttribute("- грешно въведено име!");  
            }
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.SECONDARY_SPECIALITY, new SecondarySpecialityWebModel(id + "", name, profQualificationId + "", qualificationDegreeId + "",
                    request.getParameter("dateFrom"), request.getParameter("dateTo"), code));
        } else {
            int newId = nomenclaturesDataProvider.saveSecondarySpeciality(id, name, profQualificationId, qualificationDegreeId, dateFrom, dateTo, code);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            generateSecondarySpecialityWebModel(request, getNacidDataProvider().getNomenclaturesDataProvider(), nomenclaturesDataProvider.getSecondarySpeciality(newId));
            request.setAttribute(WebKeys.SECONDARY_SPECIALITY, new SecondarySpecialityWebModel(nomenclaturesDataProvider.getSecondarySpeciality(newId)));
        }
        request.getSession().removeAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY);
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_speciality_edit");


    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) { // OK
        generateSecondarySpecialityWebModel(request, getNacidDataProvider().getNomenclaturesDataProvider(), null);
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_speciality_edit");
        request.setAttribute(WebKeys.SECONDARY_SPECIALITY, new SecondarySpecialityWebModel());
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) { // OK
        int secondarySpecialityId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (secondarySpecialityId < 0) {
            throw new UnknownRecordException("Unknown secondary speciality ID:" + secondarySpecialityId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_speciality_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        SecondarySpeciality secondarySpeciality = nomenclaturesDataProvider.getSecondarySpeciality(secondarySpecialityId);
        if (secondarySpeciality == null) {
            throw new UnknownRecordException("Unknown secondary speciality ID:" + secondarySpecialityId);//
        }
        generateSecondarySpecialityWebModel(request, getNacidDataProvider().getNomenclaturesDataProvider(), secondarySpeciality);
        request.setAttribute(WebKeys.SECONDARY_SPECIALITY, new SecondarySpecialityWebModel(secondarySpeciality));
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        /**
         * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
         */
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_CODE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PROFESSIONAL_QUALIFICATION_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PROFESSIONAL_QUALIFICATION_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_QUALIFICATION_DEGREE_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_QUALIFICATION_DEGREE_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
            session.setAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY, table);
            resetTableData(request);

        }
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            //TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_NAME_IS_REGULATED, COLUMN_NAME_IS_REGULATED, request, table, tableState); //
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_PROF_QUALIFICATION, COLUMN_NAME_PROFESSIONAL_QUALIFICATION_ID, request, table, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_QUALIFICATION_DEGREE, COLUMN_NAME_QUALIFICATION_DEGREE_ID, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY + WebKeys.TABLE_STATE, tableState);
        }

        TableWebModel webmodel = new TableWebModel("Специалности - обучение");
        //webmodel.setColumnFormatter("userDate", CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideUnhideColumn(COLUMN_NAME_PROFESSIONAL_QUALIFICATION_ID, true);
        webmodel.hideUnhideColumn(COLUMN_NAME_QUALIFICATION_DEGREE_ID, true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        if (DataConverter.parseBoolean(request.getParameter("inner"))) {
            request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");
        } else {
            request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");    
        }


        //Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            
            //filtersWebModel.addFiler(generateIsRegulatedFilterWebModel(request)); ///
            filtersWebModel.addFiler(generateProfessionalQualificationFilterWebModel(request.getParameter(FILTER_NAME_PROF_QUALIFICATION), 
                    getNacidDataProvider().getNomenclaturesDataProvider(), request));
            filtersWebModel.addFiler(generateQualificationDegreeFilterWebModel(request.getParameter(FILTER_NAME_QUALIFICATION_DEGREE), 
                    getNacidDataProvider().getNomenclaturesDataProvider(), request));
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) { // OK
        int secondarySpecialityId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (secondarySpecialityId <= 0) {
            throw new UnknownRecordException("Unknown secondary speciality ID:" + secondarySpecialityId);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        SecondarySpeciality speciality = nomenclaturesDataProvider.getSecondarySpeciality(secondarySpecialityId);
        if (speciality == null) {
            throw new UnknownRecordException("Unknown secondary speciality ID:" + secondarySpecialityId);
        }
        nomenclaturesDataProvider.saveSecondarySpeciality(speciality.getId(), speciality.getName(), speciality.getProfessionalQualificationId(),
                speciality.getQualificationDegreeId(), speciality.getDateFrom(), new Date(), speciality.getCode());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_SECONDARY_SPECIALITY);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<SecondarySpeciality> specialities = nomenclaturesDataProvider.getSecondarySpecialities(null, null,
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false), null);
        if (specialities != null) {
            for (SecondarySpeciality ss : specialities) {
                try {
                    table.addRow(ss.getId(), ss.getCode(), ss.getName(), ss.getProfessionalQualificationName(), ss.getProfessionalQualificationId(),
                            ss.getQualificationDegreeName(), ss.getQualificationDegreeId(), ss.getDateFrom(), ss.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    
    private static void generateSecondarySpecialityWebModel(HttpServletRequest request, NomenclaturesDataProvider nomenclaturesDataProvider, SecondarySpeciality speciality) {
        ComboBoxUtils.generateNomenclaturesComboBox(speciality == null ? null : speciality.getProfessionalQualificationId(),
            NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_PROFESSIONAL_QUALIFICATION, nomenclaturesDataProvider, true, request, "prof_qualification_id",
            NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);

        ComboBoxUtils.generateNomenclaturesComboBox(speciality == null ? null : speciality.getQualificationDegreeId(),
            NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, nomenclaturesDataProvider, true, request, "qualification_degree_id",
            NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        
        if (speciality != null) {
            request.setAttribute(WebKeys.SECONDARY_SPECIALITY, new SecondarySpecialityWebModel(speciality));    
        }
    }
    
    private static ComboBoxFilterWebModel generateProfessionalQualificationFilterWebModel(String activeProfessionalQualificationName,
            NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeProfessionalQualificationName, true);

        List<SecondaryProfessionalQualification> professionalQualifications = nomenclaturesDataProvider.getSecondaryProfessionalQualifications(null, 
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), null);
        for (SecondaryProfessionalQualification pq:professionalQualifications) {
            combobox.addItem(pq.getId() + "", pq.getName() + (pq.isActive() ? "" : " (inactive)"));
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_PROF_QUALIFICATION, "Професионална квалификация:");
        res.setLabelOnTop(true);
        res.setElementClass("brd w300");
        return res;
    }
    
    private static ComboBoxFilterWebModel generateQualificationDegreeFilterWebModel(String activeQualificationDegreeName,
            NomenclaturesDataProvider nDP, HttpServletRequest request) {

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeQualificationDegreeName, true);

        List<FlatNomenclature> qualificationDegrees = nDP.getFlatNomenclatures(nDP.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, null, 
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        
        for (FlatNomenclature qd : qualificationDegrees) {
            combobox.addItem(qd.getId() + "", qd.getName() + (qd.isActive() ? "" : " (inactive)"));
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_QUALIFICATION_DEGREE, "Степен на професионална квалификация:");
        res.setLabelOnTop(true);
        res.setElementClass("brd w300");
        return res;
    }
    
}


