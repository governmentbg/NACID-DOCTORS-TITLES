package com.nacid.web.handlers.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.comision.ComissionMemberOrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.table.impl.FilterCriteria;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.*;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class ApplicationExpertHandler extends NacidBaseRequestHandler {

    private static final String COLUMN_NAME_NUMBER ="Делов. номер";    
    private static final String COLUMN_NAME_APPL_NAME ="Име на заявителя";
    private static final String COLUMN_NAME_UNIVERSITY ="Висше училище";
    private static final String COLUMN_NAME_COUNTRY ="Държава";
    private static final String COLUMN_NAME_SPECIALITY ="Специалност";

    private static final String COLUMN_NAME_EXP_NAME ="Име на експерт";
    private static final String COLUMN_NAME_APPL_STATUS ="Статус на заявлението";
    private static final String COLUMN_NAME_EXPERT_STATUS ="Статус на разглеждане";
    
    private static final String FILTER_NUMBER = "number";
    private static final String FILTER_APPLICANT_NAME = "appName";
    private static final String FILTER_EXP_NAME = "expName";
    
    private static final String FILTER_APPLICATION_STATUS = "applStatus";
    private static final String FILTER_EXPERT_STATUS = "expertStatus";
    
    
    public ApplicationExpertHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPLICATION_EXPERT);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            
            table.addColumnHeader(COLUMN_NAME_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);   
            table.addColumnHeader(COLUMN_NAME_APPL_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_EXP_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_APPL_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_EXPERT_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            
           // table.addColumnHeader(COLUMN_NAME_APPL_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            //table.addColumnHeader(COLUMN_NAME_APP_SESSION_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            
            session.setAttribute(WebKeys.TABLE_APPLICATION_EXPERT, table);
            resetTableData(request);

        }

        Set<String> selectedExperts = RequestParametersUtils.convertRequestParamToList(request.getParameter(FILTER_EXP_NAME + "Ids"));
        String expId = request.getParameter(FILTER_EXP_NAME);
        if (!StringUtils.isEmpty(expId) && !"-".equals(expId)) {
            if (selectedExperts == null) {
                selectedExperts = new TreeSet<String>();
            }
            selectedExperts.add(expId);
        }
        
        
        
        // TableState settings
        TableState tableState = (TableState) session
                .getAttribute(WebKeys.TABLE_APPLICATION_EXPERT + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            
            TableStateAndFiltersUtils.addStartsWithFilterToTableState(
                    FILTER_NUMBER, COLUMN_NAME_NUMBER, request, table, tableState);
            TableStateAndFiltersUtils.addContainsFilterToTableState(
                    FILTER_APPLICANT_NAME, COLUMN_NAME_APPL_NAME, request, table, tableState);
            
            if (selectedExperts != null) {
                for (String s:selectedExperts) {
                    try {
                        tableState.addFilter(COLUMN_NAME_EXP_NAME, table.getCoulmnHeader(COLUMN_NAME_EXP_NAME).getColumnCellsType(), FilterCriteria.CONDITION_CONTAINS, s);
                    } catch (CellCreationException e) {
                        throw Utils.logException(e);
                    }
                }
            }
            
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_APPLICATION_STATUS, COLUMN_NAME_APPL_STATUS, request, table, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_EXPERT_STATUS, COLUMN_NAME_EXPERT_STATUS, request, table, tableState);
            
            
            session.setAttribute(WebKeys.TABLE_APPLICATION_EXPERT + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на разпределените заявления");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setHasOperationsColumn(false);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
                .getAttribute(WebKeys.TABLE_APPLICATION_EXPERT + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            
            FilterWebModel filter = new TextFieldFilterWebModel(
                    FILTER_NUMBER, COLUMN_NAME_NUMBER, request.getParameter(FILTER_NUMBER));
            filter.setLabelOnTop(true);
            filter.setElementClass("brd");
            filtersWebModel.addFiler(filter);
            
            
            filter = new TextFieldFilterWebModel(
                    FILTER_APPLICANT_NAME, COLUMN_NAME_APPL_NAME, request.getParameter(FILTER_APPLICANT_NAME));
            filter.setLabelOnTop(true);
            filter.setElementClass("brd w200");
            filtersWebModel.addFiler(filter);
            ComissionMemberDataProvider comissionMemberDataProvider = getNacidDataProvider().getComissionMemberDataProvider();
            
            

            //generating selectedExperts filter web model!
            Map<Object, String> map = new HashMap<Object, String>();
            List<ComissionMember> commissionMembers = comissionMemberDataProvider.getComissionMembers(false, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_COMISSION_MEMBER, ComissionMemberOrderCriteria.ORDER_COLUMN_FULL_NAME, true));
            if (selectedExperts != null) {
                for (String exp:selectedExperts) {
                    for (ComissionMember m:commissionMembers) {
                        if (m.getFullName().equals(exp)) {
                            map.put(m.getFullName(), m.getFullName());        
                        }
                    }
                }
            }
            filter = new ComplexComboBoxFilterWebModel(ComboBoxUtils.generateExpertsCombobox(null, commissionMembers, request, null, true),
                    FILTER_EXP_NAME, COLUMN_NAME_EXP_NAME, map);
            filter.setLabelOnTop(true);
            filter.setElementClass("brd w200");
            filtersWebModel.addFiler(filter);
            //end of generating selectedExperts filter web model.
            
            
            
            filter = ApplicationsHandler.generateStatusFilterComboBox(FILTER_APPLICATION_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), request);
            filter.setElementClass("brd w300");
            filtersWebModel.addFiler(filter);
            
            filter = new ComboBoxFilterWebModel(CommissionApplicationsHandler.generateFinishedByExpertsCombobox(request.getParameter(FILTER_EXPERT_STATUS)), FILTER_EXPERT_STATUS, COLUMN_NAME_EXPERT_STATUS);
            filter.setLabelOnTop(true);
            filter.setElementClass("brd w400");
            filtersWebModel.addFiler(filter);
            
            session.setAttribute(WebKeys.TABLE_APPLICATION_EXPERT
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    
    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATION_EXPERT);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        
        NacidDataProvider nDP = getNacidDataProvider();
        ApplicationsDataProvider appDP = nDP.getApplicationsDataProvider();
        NomenclaturesDataProvider nomDP = nDP.getNomenclaturesDataProvider();
        
        List<Application> applications = appDP.getAssignedApplications();
        if(applications != null) {
            for(Application app : applications) {
                try {
                    String appNumber = app.getApplicationNumber();
//                    Person applicant = app.getApplicant();
//                    String applicantNames = applicant == null ? "-" : (applicant.getFName() + " " + applicant.getLName());
                    String universityName = "";
                    String country = "";
                    //String speciality = "";
                    String expertNames = "";
                    String appSessionStatus = "";
                    
                    TrainingCourse tc = app.getTrainingCourse();
                    Person owner = tc.getOwner();
                    ;

                    University university = null;
                    if(tc != null) {
                        UniversityWithFaculty uf = tc.getBaseUniversityWithFaculty();
                        university = uf == null ? null : uf.getUniversity();
                        /*Speciality spec = tc.getSpeciality();
                        if(spec != null) {
                            speciality = spec.getName();
                        }*/
                    }
                    if(university != null) {
                        universityName = university.getBgName();
                        country = nomDP.getCountry(university.getCountryId()).getName();
                    }
                    List<ApplicationExpert> experts = app.getApplicationExperts();
                    if(experts != null && experts.size() > 0) {
                        String coma = "";
                        for(ApplicationExpert ae : experts) {
                            ComissionMember comMem = ae.getExpert();
                            expertNames += coma + comMem.getFullName();
                            coma = ", ";
                        }
                        table.addRow(appNumber, owner.getFullName(),
                                universityName, country, tc.getSpecialityNamesSeparatedBySemiColon(), expertNames, app.getApplicationStatus().getName(), getFinishedByExpertsText(app)/*,
                                appSessionStatus*/);
                    }
                    else {
                        continue;
                    }
                     
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
        
    }

    private static String getFinishedByExpertsText(Application app) {
        Boolean result = app.isFinishedByexperts();
        return result == null ? "" : (result == true ? "Експертите са приключили работата по заявлението" : "Експертите още работят по заявлението");
    }
}
