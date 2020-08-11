package com.ext.nacid.web.handlers.impl;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.academicrecognition.AcademicRecognitionDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionExtendedImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;

public class PublicRegisterBGUniversityAcademicRecognitionHandler extends PublicRegisterHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_RECOGNIZED_UNIVERSITY_ID = "Университет, признал дипломата ";
    private final static String COLUMN_NAME_RECOGNIZED_UNIVERSITY = "Университет, признал дипломата";
    private final static String COLUMN_NAME_DIPLOMA_NUMBER = "Номер диплома";
    private final static String COLUMN_NAME_DIPLOMA_DATE = "Дата";
    private final static String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
    private final static String COLUMN_NAME_COUNTRY = "Държава";
    private final static String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
    private final static String COLUMN_NAME_APPLICANT = "Име";
    private final static String COLUMN_NAME_PROTOCOL_NUMBER = "Номер на протокол";
    
    
    //private final static String FILTER_NAME_CERTIFICATE_NUMBER = "certNumber";
    private static final String FILTER_NAME_NAME = "nameFilter";
    private static final String FILTER_NAME_RECOGNIZED_UNIVERSITY = "recognizedUniversityFilter";
    
    public PublicRegisterBGUniversityAcademicRecognitionHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.DONT_CHECK_TABLE_USER_OPERATION_ACCESS, true);
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_PUBLIC_REGISTER_BG_UNIVERSITY);
        
        //System.out.println("table = " + table + " onlyActive?" + onlyActive);
        boolean reloadTable = true;//RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_RECOGNIZED_UNIVERSITY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_RECOGNIZED_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DIPLOMA_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DIPLOMA_DATE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_APPLICANT, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_RECOGNIZED_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PROTOCOL_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            session.setAttribute(WebKeys.TABLE_PUBLIC_REGISTER_BG_UNIVERSITY, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_PUBLIC_REGISTER_BG_UNIVERSITY + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            //TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS, request, table, tableState);           
            
            TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_NAME, COLUMN_NAME_APPLICANT, request, table, tableState);
            
            session.setAttribute(WebKeys.TABLE_PUBLIC_REGISTER_BG_UNIVERSITY + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на признати дипломи от български университети");
        webmodel.hideUnhideColumn(COLUMN_NAME_RECOGNIZED_UNIVERSITY_ID, true);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_ALL);
        webmodel.setHasOperationsColumn(false);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        
        
        
        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_PUBLIC_REGISTER_BG_UNIVERSITY + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            //filtersWebModel.addFiler(generateStatusFilterComboBox(request.getParameter(FILTER_NAME_STATUS), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            //filtersWebModel.addFiler(generateOnlyActiveFilterWebModel(request));
            FilterWebModel f = new TextFieldFilterWebModel(FILTER_NAME_NAME, COLUMN_NAME_APPLICANT, request.getParameter(FILTER_NAME_NAME));
            f.setElementClass("brd w300");
            filtersWebModel.addFiler(f);
            f = new ComboBoxFilterWebModel(generateBGUniversitiesCombobox(DataConverter.parseInteger(request.getParameter(FILTER_NAME_RECOGNIZED_UNIVERSITY), null), getNacidDataProvider().getUniversityDataProvider(), request), FILTER_NAME_RECOGNIZED_UNIVERSITY, COLUMN_NAME_RECOGNIZED_UNIVERSITY_ID);
            f.setElementClass("brd w300");
            filtersWebModel.addFiler(f);
            session.setAttribute(WebKeys.TABLE_PUBLIC_REGISTER_BG_UNIVERSITY + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

        setNextScreen(request, "public_register_bg_academic_recognition");
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_PUBLIC_REGISTER_BG_UNIVERSITY);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        AcademicRecognitionDataProvider dp = nacidDataProvider.getAcademicRecognitionDataProvider();
        
        List<BGAcademicRecognitionExtendedImpl> records = dp.getAcademicReconitionExtended();
        //System.out.println("apps.size():" + apps.size());
        if (records != null) {
            for (BGAcademicRecognitionExtendedImpl app : records) {
                try {
                    table.addRow(app.getId(), app.getRecognizedUniversityId(), app.getRecognizedUniversityName(), app.getDiplomaNumber(), app.getDiplomaDate(), app.getApplicant(), app.getUniversity(), app.getUniversityCountry(), app.getRecognizedSpeciality(), app.getProtocolNumber());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    public static ComboBoxWebModel generateBGUniversitiesCombobox(Integer selectedUniversityId, UniversityDataProvider udp, HttpServletRequest request) {
        List<University> universities = udp.getUniversities(Country.COUNTRY_ID_BULGARIA, false);
        return ComboBoxUtils.generateComboBox(selectedUniversityId, universities, request, null, true, "getId", "getBgName");
    }
    
   
}
