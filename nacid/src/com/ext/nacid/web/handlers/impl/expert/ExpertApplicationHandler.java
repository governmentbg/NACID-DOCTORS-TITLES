package com.ext.nacid.web.handlers.impl.expert;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.ext.nacid.web.handlers.impl.applications.ExpertReportHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationExpert;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.TrainingCourse;
import com.nacid.bl.applications.University;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class ExpertApplicationHandler extends NacidExtBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
    private final static String COLUMN_NAME_COUNTRY = "Държава";
    private final static String COLUMN_NAME_SPECIALITY = "Специалност";
    private final static String COLUMN_NAME_NAME = "Име";
    //private final static String COLUMN_NAME_EMAIL = "Електронна поща";
    private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
    private final static String COLUMN_NAME_APP_STATUS_ID = "statusId";
    private final static String COLUMN_NAME_PROCESS_STAT = "Работа по заявлението";
    
    
    public ExpertApplicationHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("id"), -1);
        
        Integer activeForm = DataConverter.parseInteger(
                request.getParameter("activeForm"), 1);
        request.setAttribute(WebKeys.EXPERT_APPLICATION_FORM_ID, activeForm);
        request.setAttribute("applicationId", applicationId);
        UserAccessUtils.checkExpertAccessToApplication(getLoggedUser(request, response), applicationId, getNacidDataProvider());
        //Generirane na taba sys zaqvlenieto
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ComissionMemberDataProvider cmdp = nacidDataProvider.getComissionMemberDataProvider();
        User user = getLoggedNacidUser(request, response);
        ApplicationExpert ae = getNacidDataProvider().getApplicationsDataProvider()
            .getApplicationExpert(applicationId, cmdp.getComissionMemberByUserId(user.getUserId()).getId());
        
        request.setAttribute("applicId", applicationId);
        if(ae != null) {
            request.setAttribute("finishedSelected", 
                    ae.getProcessStat() == 0 ? "" : "checked=\"checked\"");
        }
        
        
        int internalApplicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        ComissionMemberDataProvider comissionMemberDataProvider = nacidDataProvider.getComissionMemberDataProvider();
		ComissionMember commissionMember = comissionMemberDataProvider.getComissionMemberByUserId(getLoggedUser(request, response).getUserId());
		if (commissionMember == null) {
			throw new RuntimeException("This user is not linked to any commission_member User Id = " + getLoggedUser(request, response).getUserId() + ")");
		}
		List<Integer> applicationsByExpert = comissionMemberDataProvider.getApplicationIdsByExpert(commissionMember.getId());
		if (applicationsByExpert == null || !applicationsByExpert.contains(internalApplicationId)) {
			throw new RuntimeException("You are not authorized to view this application");
		}
		
		ExpertReportHandler.prepareExpertReport(request, response, nacidDataProvider, internalApplicationId);
        
		new ExpertStatementsHandler(getServletContext()).handleList(request, response);
        setNextScreen(request, "application_expert_edit");
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int appId = DataConverter.parseInt(request.getParameter("id"), -1);
        boolean finished = DataConverter.parseBoolean(request.getParameter("finished"));
        
        ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();
        ComissionMemberDataProvider cmdp = getNacidDataProvider().getComissionMemberDataProvider();
        User user = getLoggedNacidUser(request, response);
        appDP.setExpertProcessStatus(appId, 
                cmdp.getComissionMemberByUserId(user.getUserId()).getId(), finished ? 1 : 0);
        
        addSystemMessageToSession(request, "expFinishedSM", 
                new SystemMessageWebModel("Данните са записани в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        
        try {
            response.sendRedirect(request.getContextPath() +
                    "/control/expert_application/edit?id="+appId+"&activeForm=2");
        }
        catch (IOException e) {
            Utils.logException(e);
        }
    }
    
   
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        
        request.setAttribute(WebKeys.DONT_CHECK_TABLE_USER_OPERATION_ACCESS, true);
        
        HttpSession session = request.getSession();
        
        Table table = (Table) session.getAttribute(WebKeys.TABLE_EXPERT_APPLICATIONS);
        //System.out.println("table = " + table + " onlyActive?" + onlyActive);
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

            //table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_APP_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_PROCESS_STAT, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            session.setAttribute(WebKeys.TABLE_EXPERT_APPLICATIONS, table);
            resetTableData(request, response);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_EXPERT_APPLICATIONS + WebKeys.TABLE_STATE);
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        
        if (tableState == null || !getLastTableState) {
            boolean emptyTableState = tableState == null || reloadTable;
        	tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            //Samo v pyrvona4alniq moment trqbva da sloji filtrirane po tazi kolona!
        	if (emptyTableState) {
            	tableState.setOrderCriteria(COLUMN_NAME_PROCESS_STAT, true);	
            }
        	
            session.setAttribute(WebKeys.TABLE_EXPERT_APPLICATIONS + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Заявления");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_VIEW);
        webmodel.hideUnhideColumn(COLUMN_NAME_APP_STATUS_ID, true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        
        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_EXPERT_APPLICATIONS + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            //filtersWebModel.addFiler(generateStatusFilterComboBox(request.getParameter(FILTER_NAME_STATUS), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            //filtersWebModel.addFiler(generateOnlyActiveFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_EXPERT_APPLICATIONS + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

        setNextScreen(request, "table_list");
    }

    private void resetTableData(HttpServletRequest request, HttpServletResponse response) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EXPERT_APPLICATIONS);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        ComissionMemberDataProvider cmdp = nacidDataProvider.getComissionMemberDataProvider();
        ComissionMember expert = cmdp.getComissionMemberByUserId(getLoggedUser(request, response).getUserId());
        if(expert == null) {
            return;
        }
        List<Application> apps = applicationsDataProvider.getApplicationsForExpert(
                expert.getId());
        
        if (apps != null) {
            for (Application app : apps) {
                try {
                    List<ApplicationExpert> aes = app.getApplicationExperts();
                    int stat = 0;
                    for(ApplicationExpert ae : aes) {
                        if(ae.getExpertId() == expert.getId()) {
                            stat = ae.getProcessStat();
                        }
                    }
                    String processStat = stat != 0 ? "приключена" : "неприключена";
                    
                    TrainingCourse trainingCourse = app.getTrainingCourse();
                    //Speciality speciality = trainingCourse == null ? null : trainingCourse.getSpeciality();
                    UniversityWithFaculty uf = app.getTrainingCourse().getBaseUniversityWithFaculty();
                    University university = uf == null ? null : uf.getUniversity();
                    Country country = university == null ? null : nomenclaturesDataProvider.getCountry(university.getCountryId());
                    table.addRow(app.getId(), app.getApplicationNumber(), app.getApplicationDate(), app.getApplicant().getFullName(), university == null ? "-" : university
                                    .getBgName(), country == null ? "-" : country.getName(), trainingCourse.getSpecialityNamesSeparatedBySemiColon(), /*app.getEmail(),*/ 
                                    app.getApplicationStatus().getName(), 
                                    app.getApplicationStatusId(), processStat);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

   
}
