package com.ext.nacid.regprof.web.handlers.impl;

import com.ext.nacid.web.handlers.NacidExtNoAuthorizationCheckBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationForPublicRegister;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegprofPublicRegisterHandler extends NacidExtNoAuthorizationCheckBaseRequestHandler {

    public final static int REGISTER_TYPE_IZDADENI = 1;
    public final static int REGISTER_TYPE_OTKAZI = 2;
    public final static int REGISTER_TYPE_OBEZSILENI = 3;
    
    public final static Map<Integer, List<Integer>> REGISTER_TYPE_TO_FINAL_STATUS = new HashMap<Integer, List<Integer>>();
    static {
        REGISTER_TYPE_TO_FINAL_STATUS.put(REGISTER_TYPE_IZDADENI, Arrays.asList(ApplicationStatus.APPLICATION_RECOGNIZED_QUALIFICATION_STATUS_CODE, ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE, ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE));
        REGISTER_TYPE_TO_FINAL_STATUS.put(REGISTER_TYPE_OTKAZI, Arrays.asList(ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE));
        REGISTER_TYPE_TO_FINAL_STATUS.put(REGISTER_TYPE_OBEZSILENI, Arrays.asList(ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE));
    }
    
    public final static Map<Integer, String> registerType2Name = new HashMap<Integer, String>();
    static {
        registerType2Name.put(REGISTER_TYPE_IZDADENI, "Публичен регистър на издадените удостоверения");
        registerType2Name.put(REGISTER_TYPE_OTKAZI, "Публичен регистър на отказите");
        registerType2Name.put(REGISTER_TYPE_OBEZSILENI, "Публичен регистър на обезсилените удостоверения");
    }
    //RayaWritten------------------------------------------------------------------------------------
    public final static Map<Integer, int[]> registerType2DocTypes = new HashMap<Integer, int[]>();
    static {
        registerType2DocTypes.put(REGISTER_TYPE_IZDADENI, new int[]{DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_CPO, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_SDK,
                DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_STAJ});
        registerType2DocTypes.put(REGISTER_TYPE_OTKAZI, new int[]{DocumentType.DOC_TYPE_REGPROF_OTKAZ_1, DocumentType.DOC_TYPE_REGPROF_OTKAZ_2,
                DocumentType.DOC_TYPE_REGPROF_OTKAZ_3});
    }
    //----------------------------------------------------------------------------------------------------
    
    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_COUNTRY = "Кандидатства за държава";
    private final static String COLUMN_NAME_NAME = "Име";
    private final static String COLUMN_NAME_CERTIFICATE_NUMBER = "Номер на удостоверение";

    
    private final static String FILTER_NAME_CERTIFICATE_NUMBER = "certNumber";
    private static final String FILTER_NAME_NAME = "nameFilter";
    
    public RegprofPublicRegisterHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        
        int registerType = DataConverter.parseInt(request.getParameter("type"), 0);
                
        request.setAttribute(WebKeys.DONT_CHECK_TABLE_USER_OPERATION_ACCESS, true);
        request.setAttribute("isCertificateType", registerType == REGISTER_TYPE_IZDADENI || registerType == REGISTER_TYPE_OBEZSILENI);
        
        HttpSession session = request.getSession();
        
        Table table = (Table) session.getAttribute(WebKeys.TABLE_REGPROF_PUBLIC_REGISTER);
        //System.out.println("table = " + table + " onlyActive?" + onlyActive);
        boolean reloadTable = true;//RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            table.addColumnHeader(COLUMN_NAME_CERTIFICATE_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            

    
            session.setAttribute(WebKeys.TABLE_REGPROF_PUBLIC_REGISTER, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_REGPROF_PUBLIC_REGISTER + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            //TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS, request, table, tableState);           
            
            if (registerType != REGISTER_TYPE_OTKAZI) {
            	TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_NAME_CERTIFICATE_NUMBER, COLUMN_NAME_CERTIFICATE_NUMBER, request, table, tableState);	
            }
            TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_NAME, COLUMN_NAME_NAME, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_REGPROF_PUBLIC_REGISTER + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        request.setAttribute("registerName", registerType2Name.get(registerType));
        TableWebModel webmodel = new TableWebModel(null);

        if (registerType == REGISTER_TYPE_OTKAZI) {
        	webmodel.hideUnhideColumn(COLUMN_NAME_CERTIFICATE_NUMBER, true);
        }
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_VIEW);
        webmodel.setHasOperationsColumn(false);
        webmodel.addFormAdditionalRequestParam("type", registerType + "");
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_VIEW, "type", registerType + "");
        webmodel.setViewOpenInNewWindow(true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        
        
        
        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_REGPROF_PUBLIC_REGISTER + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            //filtersWebModel.addFiler(generateStatusFilterComboBox(request.getParameter(FILTER_NAME_STATUS), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            //filtersWebModel.addFiler(generateOnlyActiveFilterWebModel(request));
            FilterWebModel f = new TextFieldFilterWebModel(FILTER_NAME_NAME, COLUMN_NAME_NAME, request.getParameter(FILTER_NAME_NAME));
            f.setElementClass("brd w300");
            filtersWebModel.addFiler(f);
            if (registerType != REGISTER_TYPE_OTKAZI) {
            	FilterWebModel filter = new TextFieldFilterWebModel(FILTER_NAME_CERTIFICATE_NUMBER, COLUMN_NAME_CERTIFICATE_NUMBER, request.getParameter(FILTER_NAME_CERTIFICATE_NUMBER));
            	filter.setElementClass("brd");
            	filtersWebModel.addFiler(filter);
            }
            
            session.setAttribute(WebKeys.TABLE_REGPROF_PUBLIC_REGISTER + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

        setNextScreen(request, "public_register");
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_REGPROF_PUBLIC_REGISTER);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider regprofApplicationDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
        
        int registerType = DataConverter.parseInt(request.getParameter("type"), 0);
        
        if(registerType == 0) {
            return;
        }
        //System.out.println("before getting apps...");
        List<? extends RegprofApplicationForPublicRegister> apps = regprofApplicationDataProvider.getRegprofApplicationsByFinalStatuses(REGISTER_TYPE_TO_FINAL_STATUS.get(registerType));
        //System.out.println("apps.size():" + apps.size());
        if (apps != null) {
            for (RegprofApplicationForPublicRegister app : apps) {
                try {
                    
                    List<String> certNumbers;
                    if (registerType == REGISTER_TYPE_OBEZSILENI) {
                        certNumbers = app.getInvalidatedCertNumbers();
                    } else {
                        certNumbers = Arrays.asList(app.getValidatedCertNumber());
                    }
                    //TODO:Tuk na drugoto vynshno prilojenie zapisi se dobavqha samo ako certNumbers.size() != 0
                    //Moje bi i tuk shte e taka, no kym momenta getRegprofApplicationsByFinalStatus ne vry6ta nikakvi certifikate numbers, promenih koda da slaga null za certNumber!!!
                    if (certNumbers != null) {
                        for (String cn : certNumbers) {
                            table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getApplicantName(), app.getApplicationCountryName(), cn);
                        }
                    }
                    
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

   
}
