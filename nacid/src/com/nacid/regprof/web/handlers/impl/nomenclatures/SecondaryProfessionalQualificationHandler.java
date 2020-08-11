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
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.regprof.SecondaryProfessionalQualificationWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
//RayaWritten----------------------------------------------------------------
public class SecondaryProfessionalQualificationHandler extends RegProfBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_CODE = "Код";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_PROF_GROUP_NAME = "Професионално направление";
    private static final String COLUMN_NAME_PROF_GROUP_ID = "Професионално направление_id";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";

    private static final String FILTER_NAME_PROF_GROUP_ID = "profGroupFilter";

    public SecondaryProfessionalQualificationHandler(ServletContext servletContext) {
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
            table.addColumnHeader(COLUMN_NAME_PROF_GROUP_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PROF_GROUP_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);

            session.setAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION, table);
            resetTableData(request);

        }

        //TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_PROF_GROUP_ID, COLUMN_NAME_PROF_GROUP_ID, request, table, tableState);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION + WebKeys.TABLE_STATE, tableState);
        }

        //TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Професионални квалификации - обучение");
        //webmodel.setColumnFormatter("userDate", CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideUnhideColumn(COLUMN_NAME_PROF_GROUP_ID, true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        //Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(generateProfessionGroupFilterComboBox(request.getParameter(FILTER_NAME_PROF_GROUP_ID), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response){
        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
            throw new UnknownRecordException("Unknown SecondaryProfessionalQualification ID:" + id);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_prof_qualification_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        SecondaryProfessionalQualification spq = nomenclaturesDataProvider.getSecondaryProfessionalQualification(id);


        if (spq == null) {
            throw new UnknownRecordException("Unknown SecondaryProfessionalQualification ID:" + id);
        }
        generateProfessionGroupComboBox(spq.getProfessionGroupId() , nomenclaturesDataProvider, request);
        request.setAttribute(WebKeys.SECONDARY_PROFESSIONAL_QUALIFICATION, new SecondaryProfessionalQualificationWebModel(spq));
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response){
        generateProfessionGroupComboBox(0, getNacidDataProvider().getNomenclaturesDataProvider(), request);
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_prof_qualification_edit");
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
        Integer professionGroupId = DataConverter.parseInteger(request.getParameter("prof_group_id"), null);
        String code = request.getParameter("code");
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getSecondaryProfessionalQualification(id) == null) {
            throw new UnknownRecordException("Unknown SecondaryProfessionalQualification ID:" + id);
        }
        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            webmodel.addAttribute("- грешно въведено име!");
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.SECONDARY_PROFESSIONAL_QUALIFICATION, new SecondaryProfessionalQualificationWebModel(id, name, request.getParameter("dateFrom"), request.getParameter("dateTo"), code));
        } else {
            int newId = nomenclaturesDataProvider.saveSecondaryProfessionalQualification(id, name, professionGroupId, dateFrom, dateTo, code);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            request.setAttribute(WebKeys.SECONDARY_PROFESSIONAL_QUALIFICATION, new SecondaryProfessionalQualificationWebModel(nomenclaturesDataProvider.getSecondaryProfessionalQualification(newId)));
        }
        generateProfessionGroupComboBox(professionGroupId, nomenclaturesDataProvider, request);
        request.setAttribute(WebKeys.NEXT_SCREEN, "secondary_prof_qualification_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response){
        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
          throw new UnknownRecordException("Unknown SecondaryProfessionalQualification ID:" + id);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        SecondaryProfessionalQualification secondaryProfessionalQualification = nomenclaturesDataProvider.getSecondaryProfessionalQualification(id);
        if (secondaryProfessionalQualification == null) {
          throw new UnknownRecordException("Unknown SecondaryProfessionalQualification ID:" + id);
        }
        nomenclaturesDataProvider.saveSecondaryProfessionalQualification(secondaryProfessionalQualification.getId(), secondaryProfessionalQualification.getName(),
                secondaryProfessionalQualification.getProfessionGroupId(), secondaryProfessionalQualification.getDateFrom(), Utils.getToday(),
                secondaryProfessionalQualification.getCode());
        FlatNomenclatureHandler.refreshCachedNomenclatures(request);
        request.getSession().removeAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_SECONDARY_PROFESSIONAL_QUALIFICATION);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<SecondaryProfessionalQualification> qualifications = nomenclaturesDataProvider.
        getSecondaryProfessionalQualifications(null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false), null);

        if (qualifications != null) {
            for (SecondaryProfessionalQualification q:qualifications) {
                try {
                    table.addRow(q.getId(), q.getCode(), q.getName(), q.getProfessionGroupName(), q.getProfessionGroupId(), q.getDateFrom(),  q.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    private static void generateProfessionGroupComboBox(Integer activeProfessionGroypId, NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        /**
         * popylvane na education area combo box-a
         */
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeProfessionGroypId == null ? null : activeProfessionGroypId + "", true);
        List<SecondaryProfessionGroup> professionGroups = nomenclaturesDataProvider.getSecondaryProfessionGroups(null, 
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        if (professionGroups != null) {
            for (FlatNomenclature pg:professionGroups) {
                combobox.addItem(pg.getId() + "", pg.getName() + (pg.isActive() ? "" : " (inactive)"));
            }
            request.setAttribute(WebKeys.COMBO, combobox);
        }
    }
    private static ComboBoxFilterWebModel generateProfessionGroupFilterComboBox(String activeProfessionGroupName, NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeProfessionGroupName, true);

        List<SecondaryProfessionGroup> professionGroups = nomenclaturesDataProvider.getSecondaryProfessionGroups(null, 
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        if (professionGroups != null) {
            for (FlatNomenclature pg:professionGroups) {
                combobox.addItem(pg.getId() + "", pg.getName() + (pg.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_PROF_GROUP_ID, "Професионално направление:");
        res.setElementClass("brd w500");
        return res;
    }
}
//------------------------------------------------------------------------------------