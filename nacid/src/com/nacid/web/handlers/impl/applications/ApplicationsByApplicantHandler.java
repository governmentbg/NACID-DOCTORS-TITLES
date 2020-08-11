package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.TrainingCourse;
import com.nacid.bl.applications.University;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ApplicationsByApplicantHandler extends NacidBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
    private final static String COLUMN_NAME_COUNTRY = "Държава";
    private final static String COLUMN_NAME_SPECIALITY = "Специалност";
    private final static String COLUMN_NAME_NAME = "Име";
    private final static String COLUMN_NAME_EMAIL = "Електронна поща";
    private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
    
    private static final String FILTER_NAME_STATUS = "statusFilter";
    
    public ApplicationsByApplicantHandler(ServletContext servletContext) {
        super(servletContext);
    }
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
    	new ApplicationsHandler(request.getSession().getServletContext()).handleView(request, response);
    	//request.setAttribute("back_screen", "applications_by_applicant");
    	
    }
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPLICATIONS + "byApplicant");
        int applicantId = DataConverter.parseInt(request.getParameter("applicant_id"), 0);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);

            table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);

            session.setAttribute(WebKeys.TABLE_APPLICATIONS + "byApplicant", table);
            resetTableData(request, applicantId);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_APPLICATIONS + "byApplicant" + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS + "byApplicant", COLUMN_NAME_APP_STATUS, request, table, tableState);           
            session.setAttribute(WebKeys.TABLE_APPLICATIONS + "byApplicant" + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък със заявления");
        

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_APPLICATIONS + "byApplicant" + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(generateStatusFilterComboBox(request.getParameter(FILTER_NAME_STATUS + "byApplicant"), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            session.setAttribute(WebKeys.TABLE_APPLICATIONS + "byApplicant" + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }

    private void resetTableData(HttpServletRequest request, int applicantId) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATIONS + "byApplicant");
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        List<Application> apps = applicationsDataProvider.getApplicationsByApplicant(applicantId);

        if (apps != null) {
            for (Application app : apps) {
                try {
                    TrainingCourse trainingCourse = app.getTrainingCourse();
                    //Speciality speciality = trainingCourse == null ? null : trainingCourse.getSpeciality();
                    UniversityWithFaculty uf = app.getTrainingCourse().getBaseUniversityWithFaculty();
                    University university = uf == null ? null : uf.getUniversity();
                    Country country = university == null ? null : nomenclaturesDataProvider.getCountry(university.getCountryId());
                    table.addRow(app.getId(), app.getApplicationNumber(), app.getApplicationDate(), app.getApplicant().getFullName(), university == null ? "-" : university
                                    .getBgName(), country == null ? "-" : country.getName(), trainingCourse.getSpecialityNamesSeparatedBySemiColon(), app
                                    .getEmail(), app.getApplicationStatus().getName());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    
    private static ComboBoxFilterWebModel generateStatusFilterComboBox(String activeCountryName,
            NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryName, true);

        List<ApplicationStatus> statuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);
        if (statuses != null) {
            for (FlatNomenclature s : statuses) {
                combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS);
        res.setElementClass("brd");
        return res;
    }

   

    

}
