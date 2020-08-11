package com.ext.nacid.web.handlers.impl;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationForPublicRegister;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.UserOperationsUtils;
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

public class PublicRegisterHandler extends NacidExtBaseRequestHandler {

    public final static int REGISTER_TYPE_IZDADENI = 1;
    public final static int REGISTER_TYPE_OTKAZI = 2;
    public final static int REGISTER_TYPE_OBEZSILENI = 3;

   public final static Map<Integer, Integer> registerType2FinalStatus = new HashMap<Integer, Integer>();
    static {
        registerType2FinalStatus.put(REGISTER_TYPE_IZDADENI, ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE);
        registerType2FinalStatus.put(REGISTER_TYPE_OTKAZI, ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE);
        registerType2FinalStatus.put(REGISTER_TYPE_OBEZSILENI, ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE);
    }
    
    public final static Map<Integer, String> registerType2Name = new HashMap<Integer, String>();
    static {
        registerType2Name.put(REGISTER_TYPE_IZDADENI, "Публичен регистър на издадените удостоверения");
        registerType2Name.put(REGISTER_TYPE_OTKAZI, "Публичен регистър на отказите");
        registerType2Name.put(REGISTER_TYPE_OBEZSILENI, "Публичен регистър на обезсилените удостоверения");
    }
    
    public final static Map<Integer, int[]> registerType2DocTypes = new HashMap<Integer, int[]>();
    static {
        registerType2DocTypes.put(REGISTER_TYPE_IZDADENI, new int[]{DocumentType.DOC_TYPE_CERTIFICATE});
        registerType2DocTypes.put(REGISTER_TYPE_OTKAZI, new int[]{DocumentType.DOC_TYPE_REFUSE, DocumentType.DOC_TYPE_REFUSE_IN_BG});
        registerType2DocTypes.put(REGISTER_TYPE_OBEZSILENI, new int[]{DocumentType.DOC_TYPE_OBEZSILENO});
    }
    
    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
    private final static String COLUMN_NAME_COUNTRY = "Държава";
    private final static String COLUMN_NAME_SPECIALITY = "Специалност";
    private final static String COLUMN_NAME_NAME = "Име";
    private final static String COLUMN_NAME_CERTIFICATE_NUMBER = "Номер на удостоверение";

    
    private final static String FILTER_NAME_CERTIFICATE_NUMBER = "certNumber";
    private static final String FILTER_NAME_NAME = "nameFilter";
    
    public PublicRegisterHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String operationName = getOperationName(request);
        int operationId = UserOperationsUtils.getOperationId(operationName);

        if (operationId == UserOperationsUtils.OPERATION_LEVEL_NEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Добавяне на");
            handleNew(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_EDIT) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleEdit(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_VIEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Преглед на");
            handleView(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_SAVE) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleSave(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_LIST) {
            handleList(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_DELETE) {
            handleDelete(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_PRINT) {
            handlePrint(request, response);
        } else {
            handleDefault(request, response);
        }

    }
    
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        
        int registerType = DataConverter.parseInt(request.getParameter("type"), 0);
                
        request.setAttribute(WebKeys.DONT_CHECK_TABLE_USER_OPERATION_ACCESS, true);
        request.setAttribute("isCertificateType", registerType == REGISTER_TYPE_IZDADENI || registerType == REGISTER_TYPE_OBEZSILENI);
        
        HttpSession session = request.getSession();
        
        Table table = (Table) session.getAttribute(WebKeys.TABLE_PUBLIC_REGISTER);
        //System.out.println("table = " + table + " onlyActive?" + onlyActive);
        boolean reloadTable = true;//RequestParametersUtils.getParameterReloadTable(request);
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
            table.addColumnHeader(COLUMN_NAME_CERTIFICATE_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            

    
            session.setAttribute(WebKeys.TABLE_PUBLIC_REGISTER, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_PUBLIC_REGISTER + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            //TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS, request, table, tableState);           
            
            if (registerType != REGISTER_TYPE_OTKAZI) {
            	TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_NAME_CERTIFICATE_NUMBER, COLUMN_NAME_CERTIFICATE_NUMBER, request, table, tableState);	
            }
            TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_NAME, COLUMN_NAME_NAME, request, table, tableState);
            
            session.setAttribute(WebKeys.TABLE_PUBLIC_REGISTER + WebKeys.TABLE_STATE, tableState);
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
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_PUBLIC_REGISTER + WebKeys.FILTER_WEB_MODEL);
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
            
            session.setAttribute(WebKeys.TABLE_PUBLIC_REGISTER + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

        setNextScreen(request, "public_register");
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_PUBLIC_REGISTER);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        
        int registerType = DataConverter.parseInt(request.getParameter("type"), 0);
        
        if(registerType == 0) {
            return;
        }
        //System.out.println("before getting apps...");
        List<? extends ApplicationForPublicRegister> apps = applicationsDataProvider.getApplicationsByFinalStatus(
                registerType2FinalStatus.get(registerType));
        //System.out.println("apps.size():" + apps.size());
        if (apps != null) {
            for (ApplicationForPublicRegister app : apps) {
                try {
                    /*TrainingCourse trainingCourse = app.getTrainingCourse();
                    List<Speciality> specialities = trainingCourse == null ? null : trainingCourse.getRecognizedSpecialities();
                    List<String> spec = new ArrayList<String>();
                    if (specialities != null && specialities.size() != 0) {
                        for (Speciality s:specialities) {
                            spec.add(s.getName());
                        }
                    }
                    University university = app.getTrainingCourse().getBaseUniversity();
                    Country country = university == null ? null : nomenclaturesDataProvider.getCountry(university.getCountryId());
                    int id = app.getId();
                    String appNumber = app.getApplicationNumber();
                    Date appDate = app.getApplicationDate();
                    String appName = app.getApplicant().getFullName();
                    String uniName = university == null ? "-" : university.getBgName();
                    String countryName = country == null ? "-" : country.getName();
                    String specialityName = StringUtils.join(spec, ", ");
                    List<String> certificateNumbers = null;
                    if (registerType == REGISTER_TYPE_OBEZSILENI) { //vzemat se vsi4ki obezsileni udostovereniq
                    	certificateNumbers = app.getInvalidatedCertificateNumbers();
                    } else { //v arraylist-a se slaga app.getCertificateNumber();
                    	certificateNumbers = Arrays.asList(app.getCertificateNumber());
                    }
                    if (certificateNumbers != null) {
                    	for (String certNum:certificateNumbers) {
                    		table.addRow(id, appNumber, appDate, appName, uniName, countryName, specialityName, certNum);		
                    	}
                    }*/
                    List<String> certNumbers;
                    if (registerType == REGISTER_TYPE_OBEZSILENI) {
                        certNumbers = app.getInvalidatedCertNumbers();
                    } else {
                        certNumbers = Arrays.asList(app.getValidatedCertNumber());
                    }
                    if (certNumbers != null) {
                        for (String cn:certNumbers) {
                            table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getApplicantName(), app.getUniversityName(), app.getUniversityCountry(), app.getRecognizedSpecialityName(), cn);    
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
