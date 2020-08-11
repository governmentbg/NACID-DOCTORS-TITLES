package com.nacid.regprof.web.handlers.impl.applications;

import com.ext.nacid.regprof.web.handlers.impl.applications.RegprofApplicantReportHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.Company;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsImpl;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.impl.users.regprof.ResponsibleUserImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.bl.users.regprof.ResponsibleUser;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.applications.PersonRecord;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
import com.nacid.data.users.ResponsibleUserRecord;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.report.JasperReportGenerator;
import com.nacid.report.export.ExportType;
import com.nacid.report.export.JasperReportNames;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//RayaWritten-----------------------------------------------------------
public class RegprofApplicationHandler extends RegProfBaseRequestHandler {

    public static final int FORM_ID_APPLIED_DATA = 0;
    public static final int FORM_ID_APPLICATION_DATA = 1;
    public static final int FORM_ID_TRAINING_DATA = 2;
    public static final int FORM_ID_ATTACHMENTS_DATA = 3;
    public static final int FORM_ID_EXAMINATION_DATA = 4;
    public static final int FORM_ID_FINALIZATION_DATA = 5;

    private static final int MAX_FORMS_COUNT = 5;

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private static final String COLUMN_NAME_DATE = "Дата";
    private static final String COLUMN_NAME_NAME = "Име";
    private static final String COLUMN_NAME_IMI = "IMI";
    private static final String COLUMN_NAME_APP_STATUS_NAME = "Статус на заявлението";
    private static final String COLUMN_NAME_DOCFLOW_STATUS_NAME = "Деловоден статус на заявлението";
    private static final String COLUMN_NAME_APP_STATUS_ID = "statusId";
    private static final String COLUMN_NAME_ESIGNED = "Ел. подп.";
    private static final String COLUMN_NAME_END_DATE = "Срок на изпълнение";

    private static final String FILTER_NAME_APP_NUMBER = "appNumber";
    private static final String FILTER_NAME_STATUS = "statusFilter";
    private static final String FILTER_NAME_DOCFLOW_STATUS = "docflowStatusFilter";
    private static final String FILTER_NAME_ONLY_ACTIVE = "onlyActive";

    public static final String ATTRIBUTE_NAME = RegprofApplicationImpl.class.getName();
    private static final String NEXT_SCREEN = "regprofapplication";
    private static final String SYSTEM_MESSAGE = "applicationSystemMessage";
    private static final String APPLICATION_HEADER = "application_header";

    private ServletContext servletContext;

    public RegprofApplicationHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        List<Country> countries = nDP.getCountries(null, null);
        List<ServiceType> serviceTypes = nDP.getServiceTypes(null, null, null);
        RegprofApplication record = new RegprofApplicationImpl();
        record.setApplicantDocuments(new PersonDocumentRecord());
        record.setApplicationDetails(new RegprofApplicationDetailsImpl());
        record.setTrCourseDocumentPersonDetails(new RegprofTrainingCourseDetailsImpl());
        //Set empty applicant and representative
        record.setApplicant(new PersonRecord());
        record.setRepresentative(new PersonRecord());
        record.setId(0);
        request.setAttribute(ATTRIBUTE_NAME, record);
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN); 
        request.setAttribute(APPLICATION_HEADER, "Добавяне на ново заявление");

        ComboBoxUtils.generateNomenclaturesRadioButton(CivilIdType.CIVIL_ID_TYPE_EGN, nDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "applicantCivilIdType");
        ComboBoxUtils.generateNomenclaturesRadioButton(CivilIdType.CIVIL_ID_TYPE_EGN, nDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "representativeCivilIdType");
        ComboBoxUtils.generateNomenclaturesRadioButton(CivilIdType.CIVIL_ID_TYPE_EGN, nDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "documentCivilIdType");

        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, countries, true, request, "applicant.birthCountryId", true);
        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, countries, true, request, "applicationDetails.applicantCountryId", false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "applicationDetails.applicationCountryId", true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "documentRecipient.countryId", false);
        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, countries, true, request, "applicant.citizenshipId", true);
        ComboBoxUtils.generateNomenclaturesComboBox(ServiceType.SERVICE_TYPE_STANDARD, serviceTypes, true, request, "applicationDetails.serviceTypeId", false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nacidDataProvider.getNomenclaturesDataProvider(), false, request, "applicationDetails.applicantPersonalIdDocumentType", null, true);
        generateCompaniesCombo(request, nacidDataProvider, null);
        ComboBoxUtils.generateUsersComboBox(null, nacidDataProvider.getUsersDataProvider().getUsers(0, 0, 1, NacidDataProvider.APP_NACID_REGPROF_ID), request, "responsibleUsersCombo", true, false);
        ComboBoxUtils.generateDocumentReceiveMethodRadioButton(record.getApplicationDetails().getDocumentReceiveMethodId(), nDP.getDocumentReceiveMethods(null, null), true, request, "documentReceiveMethod");

        String endDate = DataConverter.formatDate(getNacidDataProvider().getRegprofApplicationDataProvider().calculateApplicationEndDate(new Date(), null));
        request.setAttribute("endDate", endDate);

        request.setAttribute("personal_data_usage", "checked=\'checked\'");
        request.setAttribute("personal_declaration", "checked=\'checked\'"); 
        String europeanCommissionWebSite = getNacidDataProvider().getUtilsDataProvider().getCommonVariableValue("europeanCommissionWebSite");
        request.setAttribute("europeanCommissionWebSite", europeanCommissionWebSite);
        request.setAttribute(WebKeys.REGPROF_OPERATION_TYPE, "new");
        request.setAttribute(WebKeys.BACK_SCREEN, getGroupName(request));
        request.setAttribute("regData", "noData"); //Telling the jsp that there is no data to make an examination - so the div won't show up
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Integer id = DataConverter.parseInt(request.getParameter("appId"), -1);
        if (id == -1) {
            id = DataConverter.parseInt(request.getParameter("id"), -1);
            if (id <= 0) {
                id = (Integer) request.getAttribute("id");
                if (id == null ||  id <= 0) {
                    throw new UnknownRecordException("Unknown regprof application ID:" + id);
                }
            }
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        RegprofApplicationDataProvider dp = nacidDataProvider.getRegprofApplicationDataProvider();
        
        RegprofApplication record = dp.getRegprofApplication(id);
        request.setAttribute("application", record);
        PersonRecord applicant = record.getApplicant();
        PersonRecord representative = record.getRepresentative();
        RegprofCertificateNumberToAttachedDocRecord certificateNumber = dp.getCertificateNumber(id, 0);
       
        request.setAttribute(APPLICATION_HEADER, applicant.getFullName() + " " + "- Деловоден № " + record.getApplicationDetails().getAppNum() + 
                "/" + DataConverter.formatDate(record.getApplicationDetails().getAppDate()) +
                (certificateNumber != null ? "<br/>" + "Удостоверение № " + certificateNumber.getCertificateNumber() : ""));
        if (certificateNumber != null) {
            request.setAttribute("certNumber", certificateNumber.getCertificateNumber());
        }
        request.setAttribute(ATTRIBUTE_NAME, record);
        checkUncheckCheckboxes(record, request);
        
        List<Country> countries = nDP.getCountries(null, null);
        List<ServiceType> serviceTypes = nDP.getServiceTypes(null, null, null);
        RegprofTrainingCourseDataProvider tcDp = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        RegprofTrainingCourseDetailsBase tcDetails = tcDp.getRegprofTrainingCourseDetails(record.getApplicationDetails().getTrainingCourseId());
        Integer serviceType = record.getApplicationDetails().getServiceTypeId();

        ComboBoxUtils.generateNomenclaturesRadioButton(applicant.getCivilIdType(),
                nDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "applicantCivilIdType");
        ComboBoxUtils.generateNomenclaturesRadioButton(representative != null ? representative.getCivilIdType() : 1, 
                nDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "representativeCivilIdType");
        ComboBoxUtils.generateNomenclaturesRadioButton(tcDetails.getDocumentCivilIdType() != null ? tcDetails.getDocumentCivilIdType() : 1,
                nDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "documentCivilIdType");
        ComboBoxUtils.generateDocumentReceiveMethodRadioButton(record.getApplicationDetails().getDocumentReceiveMethodId(), nDP.getDocumentReceiveMethods(null, null), true, request, "documentReceiveMethod");

        ComboBoxUtils.generateNomenclaturesComboBox(record.getApplicant().getBirthCountryId(), countries, true, request, "applicant.birthCountryId", true);
        ComboBoxUtils.generateNomenclaturesComboBox(record.getApplicationDetails().getApplicantCountryId(), countries, true, request, "applicationDetails.applicantCountryId", false);
        ComboBoxUtils.generateNomenclaturesComboBox(record.getApplicationDetails().getApplicationCountryId(), countries, true, request, "applicationDetails.applicationCountryId", true);
        ComboBoxUtils.generateNomenclaturesComboBox(record.getDocumentRecipient() == null ? null : record.getDocumentRecipient().getCountryId(), countries, true, request, "documentRecipient.countryId", false);
        ComboBoxUtils.generateNomenclaturesComboBox(record.getApplicant().getCitizenshipId(), countries, true, request, "applicant.citizenshipId", true);
        ComboBoxUtils.generateNomenclaturesComboBox(serviceType != null ? serviceType: ServiceType.SERVICE_TYPE_STANDARD, serviceTypes, true, request, "applicationDetails.serviceTypeId", false);
        ComboBoxUtils.generateNomenclaturesComboBox(record.getApplicationDetails().getApplicantPersonalIdDocumentType(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nacidDataProvider.getNomenclaturesDataProvider(), false, request, "applicationDetails.applicantPersonalIdDocumentType", null, true);
        generateCompaniesCombo(request, nacidDataProvider, record.getApplicationDetails());
        ComboBoxUtils.generateUsersComboBox(null, nacidDataProvider.getUsersDataProvider().getUsers(0, 0, 1, NacidDataProvider.APP_NACID_REGPROF_ID),
                request, "responsibleUsersCombo", true, false);

        request.setAttribute("responsibleUsers", record.getResponsibleUsers());
        request.setAttribute("id", id);
        String europeanCommissionWebSite = getNacidDataProvider().getUtilsDataProvider().getCommonVariableValue("europeanCommissionWebSite");
        request.setAttribute("europeanCommissionWebSite", europeanCommissionWebSite);

        if (record.getApplicationDetails().getEndDate() == null) {
            Date endDate = dp.calculateApplicationEndDate(record.getApplicationDetails().getAppDate(), record.getApplicationDetails().getServiceTypeId());
            request.setAttribute("endDate", DataConverter.formatDate(endDate)); //To set end date if nobody has set the end date
        }

        new RegprofApplicationAttachmentHandler(servletContext).handleList(request, response);
        new RegprofTrainingCourseHandler(servletContext).handleEdit(request, response);
        new RegprofApplicationExaminationHandler(servletContext).handleEdit(request, response);
        new RegprofApplicationFinalizationHandler(servletContext).handleEdit(request, response);
        
        ExtRegprofApplicationsDataProvider extRegprofApplicationsDataProvider = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationImpl extRegprofApplication = extRegprofApplicationsDataProvider.getExtRegprofApplicationByInternalApplicationId(id);
        if (extRegprofApplication != null) {
            ExtPerson extPerson = nacidDataProvider.getExtPersonDataProvider().getExtPerson(extRegprofApplication.getApplicationDetails().getApplicantId());
            RegprofApplicantReportHandler.generateExternalApplicantReport(getNacidDataProvider(), extRegprofApplication, request, "ext", extPerson);
        }
        
        request.setAttribute(WebKeys.REGPROF_OPERATION_TYPE, "edit");
        request.setAttribute(WebKeys.BACK_SCREEN, getGroupName(request));
        request.setAttribute("app_status", record.getApplicationStatusId());
        
        request.setAttribute("imiCorrespondence", record.getApplicationDetails().getImiCorrespondence());

        new RegprofApostilleServiceHandler(request.getServletContext()).handleEdit(request, response);
    }

    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        super.handleView(request, response);
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        boolean onlyActive = isOnlyActive(request);
                
        Table table = (Table) session.getAttribute(WebKeys.TABLE_REGPROF_APPLICATION + (onlyActive ? "1" : "2"));
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_APP_STATUS_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_APP_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DOCFLOW_STATUS_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_END_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_ESIGNED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            table.addColumnHeader(COLUMN_NAME_IMI, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            session.setAttribute(WebKeys.TABLE_REGPROF_APPLICATION + (onlyActive ? "1" : "2") , table);
            resetTableData(request);
        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_REGPROF_APPLICATION + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_NAME_APP_NUMBER, COLUMN_NAME_APPLICATION_NUMBER, request, table, tableState);
            String appNumberFilterValue = request.getParameter(FILTER_NAME_APP_NUMBER);
            if (appNumberFilterValue != null && !appNumberFilterValue.isEmpty()) {
                session.setAttribute("app_number_filter_value", appNumberFilterValue);
            }
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS_NAME, request, table, tableState);           
            session.setAttribute(WebKeys.TABLE_REGPROF_APPLICATION + WebKeys.TABLE_STATE, tableState);

            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_DOCFLOW_STATUS, COLUMN_NAME_DOCFLOW_STATUS_NAME, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_REGPROF_APPLICATION + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък със заявления (рег. проф.)");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideUnhideColumn(COLUMN_NAME_APP_STATUS_ID, true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_REGPROF_APPLICATION + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            FilterWebModel appNumFilter = generateApplicationNumberFilterWebModel(request);
            filtersWebModel.addFiler(appNumFilter);


            FilterWebModel statusFilter = generateStatusFilterComboBox(FILTER_NAME_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), request);
            filtersWebModel.addFiler(statusFilter);

            ComboBoxFilterWebModel docflowStatusFilter = generateDocflowStatusFilterComboBox(FILTER_NAME_DOCFLOW_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), request);
            filtersWebModel.addFiler(docflowStatusFilter);


            filtersWebModel.addFiler(generateOnlyActiveFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_REGPROF_APPLICATION + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
        //System.out.println("Applications list Time cost:" + (new Date().getTime() - date.getTime()));
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.BACK_SCREEN, getGroupName(request));
        int activeFormId = DataConverter.parseInt(request.getParameter(WebKeys.ACTIVE_FORM), -1);
        if (activeFormId < 1 || activeFormId > MAX_FORMS_COUNT) {
            throw new UnknownRecordException("Unknown active form id:" + activeFormId);
        }
        request.getSession().removeAttribute(WebKeys.TABLE_REGPROF_APPLICATION + "1");
        request.getSession().removeAttribute(WebKeys.TABLE_REGPROF_APPLICATION + "2");
        if (activeFormId == FORM_ID_APPLICATION_DATA) {
            saveApplicationDataForm(request, response);
        } else if (activeFormId == FORM_ID_TRAINING_DATA) {
            saveTrainingCourse(request, response);
            request.setAttribute(WebKeys.ACTIVE_FORM, activeFormId);
        } else if (activeFormId == FORM_ID_EXAMINATION_DATA) {
            saveExamination(request, response);
        } else if (activeFormId == FORM_ID_FINALIZATION_DATA) {
            saveFinalization(request, response);
        }
    }

    private void saveApplicationDataForm(HttpServletRequest request, HttpServletResponse response) {
        RegprofApplication record = (RegprofApplicationImpl) request.getAttribute(ATTRIBUTE_NAME);
        if (record == null) {
            throw new UnknownRecordException("Unknown regprof application...");
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider dp = nacidDataProvider.getRegprofApplicationDataProvider();
        UsersDataProvider usersDP = nacidDataProvider.getUsersDataProvider();
        List<ResponsibleUser> appResponsibleUsers = new ArrayList<ResponsibleUser>();
        String singleUser = request.getParameter("responsibleUserCombo");
        String[] responsibleUserIds = request.getParameterValues("responsibleUser");
        boolean singleRepeats = false;
        if (responsibleUserIds != null) {
            for (String s : responsibleUserIds) {
                if (singleUser.equalsIgnoreCase(s)) {
                    singleRepeats = true;
                }
                Integer userId = DataConverter.parseInteger(s, null);
                if (userId != null) {
                    ResponsibleUserRecord ruRecord = new ResponsibleUserRecord(0, record.getId(), userId);
                    appResponsibleUsers.add(new ResponsibleUserImpl(ruRecord, usersDP));
                }
            }
        }
        if (!singleRepeats) {
            Integer userId = DataConverter.parseInteger(singleUser, null);
            if (userId != null) {
                ResponsibleUserRecord ruRecord = new ResponsibleUserRecord(0, record.getId(), userId);
                appResponsibleUsers.add(new ResponsibleUserImpl(ruRecord, usersDP));
            }
        }
        record.setResponsibleUsers(appResponsibleUsers);
        record.setNamesToUpperCase();
        record = dp.saveRegprofApplicationRecord((RegprofApplicationImpl) record, getLoggedUser(request, response).getUserId());
        
        new RegprofApplicationExaminationHandler(servletContext).saveRegulatedProfessionExamination(request, response, record.getId());        

        SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);  
        request.setAttribute(SYSTEM_MESSAGE, systemMessageWebmodel);
        record = dp.getRegprofApplication(record.getApplicationDetails().getId());
        checkUncheckCheckboxes(record, request);
        request.setAttribute(ATTRIBUTE_NAME, record);
        request.setAttribute("id", record.getId());
        handleEdit(request, response);
    }

    private void saveTrainingCourse(HttpServletRequest request, HttpServletResponse response) {
        new RegprofTrainingCourseHandler(servletContext).handleSave(request, response);
    }

    private void saveExamination(HttpServletRequest request, HttpServletResponse response) {
        new RegprofApplicationExaminationHandler(servletContext).handleSave(request, response);
    }
    
    private void saveFinalization(HttpServletRequest request, HttpServletResponse response) {
        new RegprofApplicationFinalizationHandler(servletContext).handleSave(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_REGPROF_APPLICATION + (isOnlyActive(request) ? "1" : "2"));
        table.emtyTableData();
        boolean onlyActive = isOnlyActive(request);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider dp = nacidDataProvider.getRegprofApplicationDataProvider();
        List<? extends RegprofApplicationDetailsForList> records = dp.getRegprofApplicationDetailsForList(onlyActive);
        if (records != null) {
            for (RegprofApplicationDetailsForList app : records) {
                try {
                    TableRow row = table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getFullName(),  
                            app.getStatusName(), app.getStatus(), app.getDocflowStatusName(), app.getEndDate(), app.getExtSignedDocId() != null, app.getImiCorrespondence() != null);
                    if (app.getDocflowStatusId() == ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE && (app.getServiceTypeId()!= null && app.getServiceTypeId() == ServiceType.SERVICE_TYPE_QUICK)) {
                        row.setClazz("important_row");
                    }
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    public static void generateCompaniesCombo(HttpServletRequest request, NacidDataProvider nacidDataProvider, RegprofApplicationDetailsImpl application) {      
        String selectedKey = "-";
        if (application != null && application.getRepFromCompany() != null) {
            selectedKey = application.getRepFromCompany().toString();
        }
        ComboBoxWebModel combobox = new ComboBoxWebModel(selectedKey, true);

        List<Company> list = nacidDataProvider.getCompanyDataProvider().getCompanies(false);
        if (list != null) {
            for (Company c : list) {
                String inactive = "";
                if (c.getDateTo() != null && c.getDateTo().before(new Date())) {
                    inactive = " (inactive)";
                }
                combobox.addItem("" + c.getId(), c.getName() + inactive);
            }
        }
        request.setAttribute("companiesComboBox", combobox);
    }

    private static FilterWebModel generateApplicationNumberFilterWebModel(HttpServletRequest request) {
        FilterWebModel filter = new TextFieldFilterWebModel(FILTER_NAME_APP_NUMBER, COLUMN_NAME_APPLICATION_NUMBER, request.getParameter(FILTER_NAME_APP_NUMBER));
        filter.setLabelOnTop(true);
        filter.setElementClass("brd w200");
        return filter;
    }

    private static ComboBoxFilterWebModel generateStatusFilterComboBox(String filterName, NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        String activeStatusName = request.getParameter(filterName);
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeStatusName, true);

        List<ApplicationStatus> statuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE,
                NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);

        if (statuses != null) {
            for (FlatNomenclature s : statuses) {
                combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, filterName, COLUMN_NAME_APP_STATUS_NAME);
        res.setElementClass("brd w200");
        res.setLabelOnTop(true);
        return res;
    }

    public static ComboBoxFilterWebModel generateDocflowStatusFilterComboBox(String filterName,NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        String activeStatusName = request.getParameter(filterName);
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeStatusName, true);

        List<ApplicationDocflowStatus> statuses = nomenclaturesDataProvider.getApplicationDocflowStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        if (statuses != null) {
            for (FlatNomenclature s : statuses) {
                combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, filterName, COLUMN_NAME_DOCFLOW_STATUS_NAME);
        res.setElementClass("brd w250");
        res.setLabelOnTop(true);
        return res;
    }

    public static String getGroupName(HttpServletRequest request) {
        // path info: /nacid/control/users = String[]{"","users"}
        return getPathInfoElement(request.getPathInfo(), 1);
    }

    private void checkUncheckCheckboxes(RegprofApplication record, HttpServletRequest request){
        if (record.getApplicationDetails().getRepresentativeId() != null) {
            request.setAttribute("has_representative", "checked=\"checked\"");
        }
        if (record.getApplicationDetails().getRepFromCompany() != null) {
            request.setAttribute("rep_company", "checked=\"checked\"");
        }
        if (record.getApplicationDetails().getPersonalDataUsage() != 0) {
            request.setAttribute("personal_data_usage", "checked=\"checked\"");
        }
        if (record.getApplicationDetails().getPersonalEmailInforming() != 0) {
            request.setAttribute("personal_email_informing", "checked=\"checked\"");
        }
        if (record.getApplicationDetails().getDataAuthentic() != 0 ) {
            request.setAttribute("data_authentic", "checked=\"checked\"");
        }
        if (record.getApplicationDetails().getNamesDontMatch() != 0) {
            request.setAttribute("names_dont_match", "checked=\"checked\"");
        }
    }

    public void handlePrint(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        if (applicationId == 0) {
            throw new UnknownRecordException("unknown application record with id=" + request.getParameter("id"));
        }
        RegprofApplicationDataProvider applicationsDataProvider = getNacidDataProvider().getRegprofApplicationDataProvider();
        RegprofApplicationDetailsForReport application = applicationsDataProvider.getRegprofApplicationDetailsForReport(applicationId);
        List<RegprofApplicationDetailsForReport> reports = new ArrayList<RegprofApplicationDetailsForReport>();
        reports.add(application);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JasperReportGenerator generator = new JasperReportGenerator(JasperReportNames.JASPER_REPORT_REGISTRATION, ExportType.HTML, os);
        try {
            generator.export(reports);
        } catch (Exception e) {
            throw Utils.logException(e);
        }
        try {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write( os.toString());
            writer.write("<script type=\"text/javascript\">window.print();</script>");
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    private static boolean isOnlyActive(HttpServletRequest request) {
        boolean result = false;
        //Ako e sumbit-nata forma, vzema parametyra FILTER_NAME_ONLY_ACTIVE ot neq. Ako ne e submitnata, vzema predishniq status onlyActive, zapisan v sesiqta. Ako nqma nishto i tam, vry6ta false!
        //Ideqta e 4e pri natiskane na back butona pri razglejdane na zaqvlenie, doshlo ot tablicata s ne-arhiviranite zaqvleniq, i ako ne se slaga parametyr v sesiqta, onlyActive e true, no trqbva da e false!!!! 
        if (RequestParametersUtils.getParameterFormSubmitted(request)) {
            result = DataConverter.parseBoolean(request.getParameter(FILTER_NAME_ONLY_ACTIVE));
        } else if (request.getSession().getAttribute("onlyActive") != null) {
            result = (Boolean)request.getSession().getAttribute("onlyActive");
        }
        request.getSession().setAttribute("onlyActive", result);
        return result;
    }

    
    private static CheckBoxFilterWebModel generateOnlyActiveFilterWebModel(HttpServletRequest request) {
        CheckBoxFilterWebModel result =  new CheckBoxFilterWebModel(FILTER_NAME_ONLY_ACTIVE, 
                "Покажи само в процедура",
                isOnlyActive(request)
                );
        result.setLabelOnTop(true);
        result.setElementClass("brd flt_rgt");
        return result;
    }

    public static void main(String[] args){
    }

}
//---------------------------------------------------------------------------------------------------------