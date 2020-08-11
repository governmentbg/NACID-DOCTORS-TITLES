package com.nacid.web.handlers.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.ApplicationAttachmentHandler;
import com.nacid.web.model.comission.ApplicationMotivesWebModel;
import com.nacid.web.model.comission.CommissionCalendarHeaderWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.*;

import static com.nacid.bl.nomenclatures.NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA;

public class ApplicationMotivesHandler extends NacidBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
    private final static String COLUMN_NAME_SPECIALITY = "Специалност";
    private final static String COLUMN_NAME_NAME = "Име";
    private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
    private final static String COLUMN_NAME_COUNTRY = "Държава";

    private static final String FILTER_NAME_STATUS = "statusFilter";
    
    private static final String EDIT_SCREEN = "application_motives_edit";

    
    public ApplicationMotivesHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), 0);
        int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        String backOper = request.getParameter("backOper");
        
        if(applicationId <= 0) {
            throw new UnknownRecordException("Unknown application ID:" + applicationId);
        }
        
        Application application = getNacidDataProvider()
            .getApplicationsDataProvider().getApplication(applicationId);
        if(application == null) {
            Utils.logException(new Exception("Invalid id:"+applicationId));
        }
        
        String applSummary = application.getSummary();
        
        TrainingCourse trCourse = application.getTrainingCourse();
        
        Integer recognizedEduLevel = trCourse.getRecognizedEduLevelId();
        //Integer recognizedQualification = trCourse.getRecognizedQualificationId();
        
        
        
        /*ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY,
                getNacidDataProvider().getNomenclaturesDataProvider(), 
                true, request, "recognizedSpeciality", 
                OrderCriteria.createOrderCriteria(
                        OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), 
                true);*/
        List<? extends FlatNomenclature> eduLevels = getNacidDataProvider().getNomenclaturesDataProvider().getEducationLevels(application.getApplicationType(), null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        ComboBoxUtils.generateNomenclaturesComboBox(recognizedEduLevel, eduLevels, true, request, "recognizedEduLevel", true);

        List<DocumentType> documentTypes = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentTypes(null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, false), true, Arrays.asList(DocCategory.APPLICATION_ATTACHMENTS));
        ComboBoxUtils.generateNomenclaturesComboBox(null, documentTypes, false, request, "documentType", true);
        List<Integer> allowedApplicationStatueses = new ArrayList<Integer>();
        allowedApplicationStatueses.add(application.getApplicationStatusId());
        allowedApplicationStatueses.addAll(ApplicationStatus.RUDI_APPLICATION_STATUSES_FROM_COMMISSION);
        
        ComboBoxUtils.generateNomenclaturesComboBox(application.getApplicationStatusId(), getNacidDataProvider().getNomenclaturesDataProvider().getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, allowedApplicationStatueses, null, null, false), false, request, "applicationStatus", false);
        ComboBoxUtils.generateProfessionGroupComboBox(trCourse.getRecognizedProfGroupId(), getNacidDataProvider().getNomenclaturesDataProvider().getProfessionGroups(0, null, null), true, request, "recognizedProfGroupCombo",  true);

        List<FlatNomenclature> eduAreas = getNacidDataProvider().getNomenclaturesDataProvider().getFlatNomenclatures(FLAT_NOMENCLATURE_EDUCATION_AREA, null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(trCourse.getProfGroupId() == null ? null : trCourse.getProfGroup().getEducationAreaId(), eduAreas, false, request, "recognizedProfGroupEduAreaCombo", true);
        /**
         * Ima priznata kvalifikaciq samo kogato v kursa na obuchenie e posochena kvalifikaciq
         * posochenata kvalifikaciq (kakto i eduLevel) se zapisva i v recognizedQualification pri 
         * save na trainingCourse
         */
        /*if(recognizedQualification != null) {
            ComboBoxUtils.generateNomenclaturesComboBox(recognizedQualification, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION,
                getNacidDataProvider().getNomenclaturesDataProvider(), 
                true, request, "recognizedQualification", 
                OrderCriteria.createOrderCriteria(
                        OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), 
                true);
        }*/
        NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
        List<LegalReason> legalReasons = nomenclaturesDataProvider.getLegalReasons(application.getApplicationType(), null, null, application.getApplicationStatusId());
		if (legalReasons != null) {
			Integer legalReasonId = getNacidDataProvider().getApplicationsDataProvider().getLastLegalReasonId(application.getId());
			ComboBoxUtils.generateNomenclaturesComboBox(legalReasonId, legalReasons, false, request, "legalReason", true);	
		}
        
        request.setAttribute("backOper", backOper);
       
        //RayaWritten--------------------------------------------------------------------
        ApplicationMotivesWebModel webModel = new ApplicationMotivesWebModel(application, calendarId, applSummary, 
                trCourse);
        request.setAttribute(WebKeys.APPLICATION_MOTIVES_WEB_MODEL, webModel);
        request.setAttribute("recognSpecsList", webModel.getRecognSpecs());
        List<ProfessionGroup> professionGroups = nomenclaturesDataProvider.getProfessionGroups(0, null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(null, professionGroups, true, request, "professionGroupCombo", true);
        
        //-------------------------------------------------------------------------------
        
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
    	
    	
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        Integer calendarId = DataConverter.parseInteger(request.getParameter("calendar_id"), null);
        if (calendarId == null) {
        	throw new UnknownRecordException("Unknown calendar ID" + request.getParameter("calendar_id"));
        }
        
        int applicationId = DataConverter.parseInt(request.getParameter("id"), -1);
        String motives = request.getParameter("motives");
        /*Integer recognizedSpeciality = DataConverter.parseInteger(
                request.getParameter("recognizedSpeciality"), null);*/
        Integer recognizedEduLevel = DataConverter.parseInteger(
                request.getParameter("recognizedEduLevel"), null);
        Integer recognizedQualification = DataConverter.parseInteger(request.getParameter("recognizedQualificationId"), null);
        Integer recognizedProfGroup = DataConverter.parseInteger(request.getParameter("recognizedProfGroup"), null);
        Integer legalReasonId = DataConverter.parseInteger(request.getParameter("legalReason"), null);
        int statusId = DataConverter.parseInt(request.getParameter("application_status"), 0);
        
        ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();
        TrainingCourseDataProvider tcDP = getNacidDataProvider().getTrainingCourseDataProvider();
        
        Application app = appDP.getApplication(applicationId);
        
        if(app == null ) {
            throw new UnknownRecordException("Unknown application ID:" + applicationId);
        }
        request.setAttribute(WebKeys.APPLICATION_ID, applicationId);
        String applicantInfo = statusId == ApplicationStatus.APPLICATION_POSTPONED_STATUS_CODE ? request.getParameter("applicant_info") : app.getApplicantInfo();
        
		appDP.updateMotives(app.getId(), statusId, applicantInfo, motives, legalReasonId, calendarId, getLoggedUser(request, response).getUserId());
		
		TrainingCourse tc = app.getTrainingCourse();
        tcDP.saveTrainingCourse(tc.getId(), tc.getDiplomaSeries(), tc.getDiplomaNumber(), tc.getDiplomaRegistrationNumber(), tc.getDiplomaDate(),
                tc.getDiplomaTypeId(), tc.getFName(), tc.getSName(), tc.getLName(), 
                tc.isJointDegree(), tc.getTrainingStart(), 
                tc.getTrainingEnd(), tc.getTrainingDuration(), 
                tc.getDurationUnitId(), tc.getCredits(),
                tc.getEducationLevelId(), tc.getQualificationId(), 
                tc.isRecognized(), tc.getSchoolCountryId(), 
                tc.getSchoolCity(), tc.getSchoolName(), 
                tc.getSchoolGraduationDate(), tc.getSchoolNotes(), 
                tc.getPrevDiplomaUniversityId(), tc.getPrevDiplomaEduLevelId(),
                tc.getPrevDiplomaGraduationDate(), tc.getPrevDiplomaNotes(), tc.getPrevDiplomaSpecialityId(),
                recognizedEduLevel,
                /*tc.getTrainingInstId(),*/ recognizedQualification,
                tc.getGraduationDocumentTypeId(), tc.getCreditHours(), tc.getEctsCredits(),
                tc.getOwnerId(),
                tc.getThesisTopic(),
                tc.getThesisTopicEn(), tc.getProfGroupId(), recognizedProfGroup, tc.getThesisDefenceDate(), tc.getThesisBibliography(), tc.getThesisVolume(), tc.getThesisLanguageId(), tc.getThesisAnnotation(), tc.getThesisAnnotationEn(), tc.getOriginalQualificationId());
        
        String stringUnivIds = DataConverter.parseString(request.getParameter("recognSpecIds"), null);
        String[] ids = stringUnivIds == null ? null : stringUnivIds.split(";");
        Set<Integer> result = getRecognizedSpecialitiesFromRequest(request);


        tcDP.saveRecognizedSpecialities(tc.getId(), new ArrayList<Integer>(result));
        
        request.setAttribute(WebKeys.SYSTEM_MESSAGE,
                new SystemMessageWebModel("Данните бяха въведени в базата",
                        SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        handleEdit(request, response);
    }

    
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        
        int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), 0);
        
        CommissionCalendarHeaderWebModel calheadWM = (CommissionCalendarHeaderWebModel)request.getAttribute(WebKeys.COMMISSION_CALENDAR_HEADER_WEB_MODEL);
        String operation = calheadWM.getAction();
        
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_CALENDAR_AGENDA_MOTIVES);

        //boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        boolean reloadTable = true;
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
            table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            resetTableData(table, calendarId);
            session.setAttribute(WebKeys.TABLE_CALENDAR_AGENDA_MOTIVES, table);
            
        }

        
        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
        TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS, request, table, tableState);

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel(null);

        webmodel.setGroupName("application_motives");
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        
        Object operView = request.getAttribute(WebKeys.OPERATION_VIEW);
        if(operView != null && (Boolean)operView) {
            webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        }
        
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, "calendar_id", calendarId+"");
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, "backOper", operation);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = new FiltersWebModel();
        filtersWebModel.addFiler(generateStatusFilterComboBox(request.getParameter(FILTER_NAME_STATUS), getNacidDataProvider().getNomenclaturesDataProvider(), request));
        
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }
    //Za generirane na attached doc!
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
    	int documentTypeId = DataConverter.parseInt(request.getParameter("documentType"), 0);
		int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		DocumentType docType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(documentTypeId);

		if (documentTypeId == 0 || applicationId == 0) {
			throw new RuntimeException("no document type or applicationId specified....");
		}
		int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), 0);
        CommissionCalendar calendar = getNacidDataProvider().getCommissionCalendarDataProvider().getCommissionCalendar(calendarId);
        if (calendar == null) {
        	throw new UnknownRecordException("Unknown calendarId:" + request.getParameter("calendar_id"));
        }
        if (calendar.getSessionStatusId() != SessionStatus.SESSION_STATUS_PROVEDENO) {
        	request.setAttribute("generatedDocsMessage", new SystemMessageWebModel("Статусът на комисията е различен от " + nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS, SessionStatus.SESSION_STATUS_PROVEDENO).getName(), SystemMessageWebModel.MESSAGE_TYPE_ERROR));
			handleEdit(request, response);	
			return;
        }
        
		
		ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
		Application appl = applicationsDataProvider.getApplication(applicationId);
		AttachmentDataProvider attachmentDataProvider = getNacidDataProvider().getApplicationAttachmentDataProvider();
		boolean isCert = documentTypeId == DocumentType.DOC_TYPE_CERTIFICATE;
		
		
		//availableCertificateAttachments sa ot zna4enie samo ako tipa na dokumenta e sertifikat, zashtoto toi edinstven ne moje da ima po 2 takiva zapisa!
		List<Attachment> availableCertificateAttachments = !isCert ? null : attachmentDataProvider.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_CERTIFICATE);
		boolean hasCerts = !isCert ? false : availableCertificateAttachments != null && availableCertificateAttachments.size() > 0 ;
		
		SystemMessageWebModel error = null;
		if (!attachmentDataProvider.hasAccessToGenerateDocument(appl.getApplicationStatusId(), documentTypeId)) {
			error = new SystemMessageWebModel("Не може да се генерира " + docType.getName() + " за заявление със статус " + appl.getApplicationStatus().getName(), SystemMessageWebModel.MESSAGE_TYPE_ERROR);
		} else if (isCert) {
			if (hasCerts) {
				error = new SystemMessageWebModel("Вече съществува въведен документ от тип " + docType.getName(), SystemMessageWebModel.MESSAGE_TYPE_ERROR);
			} else {
				try {
					applicationsDataProvider.generateCertificateNumber(applicationId);
				} catch (CertificateNumberGenerationException e) {
					error = new SystemMessageWebModel(e.getMessage(), SystemMessageWebModel.MESSAGE_TYPE_ERROR);
				}
			}
		} 
		if (error != null) {
			request.setAttribute("generatedDocsMessage", error);
			handleEdit(request, response);	
			return;
		}

        ApplicationAttachmentHandler.GenerateAndSaveApplicationAttachmentResponse res = ApplicationAttachmentHandler.generateAndSaveApplicationAttachment(nacidDataProvider, applicationsDataProvider.getApplicationDetailsForReport(appl.getId()), docType, getLoggedUser(request, response).getUserId());
		Attachment att = attachmentDataProvider.getAttachment(res.getAttId(), true, false);
		InputStream is = att.getContentStream();
		response.setContentType("application/msword");
		response.setHeader("content-disposition", "attachment; filename=" + att.getFileName());
		writeInputStreamToResponse(response, is);
		
   }
    
    private void resetTableData(Table table, int calendarId) {
        if (table == null) {
            return;
        }
        
        List<ApplicationForList> apps = getNacidDataProvider().getCommissionCalendarDataProvider()
                .getCommissionApplicationsForList(calendarId);
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        if (apps != null) {
            for (ApplicationForList app : apps) {
                try {
                    table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getAptName(), app.getUniName(), app.getUniCountryName(), app.getSpecialityName(),
                            app.getApnStatusName());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    
    private static ComboBoxFilterWebModel generateStatusFilterComboBox(String activeCountryName, NomenclaturesDataProvider nomenclaturesDataProvider,
            HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryName, true);

        List<ApplicationStatus> statuses = nomenclaturesDataProvider.getApplicationStatuses(
                NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(
                        OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false
        );
        if (statuses != null) {
            for (FlatNomenclature s : statuses) {
                combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
        }
        return new ComboBoxFilterWebModel(combobox, FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS);
    }


    public static Set<Integer> getRecognizedSpecialitiesFromRequest(HttpServletRequest request) {
        Set<Integer> result = new LinkedHashSet<Integer>();

        int specialitiesCount = DataConverter.parseInt(request.getParameter("recognizedSpeciality_specialities_count"), 0);
        for (int i = 1; i <= specialitiesCount; i++) {
            String param = request.getParameter("specialitiesListItem_recognizedSpeciality_" + i);
            Integer var = DataConverter.parseInteger(param, null);
            if (var != null) {
                result.add(var);
            }
        }
        Integer sp = DataConverter.parseInteger(request.getParameter("recognizedSpecialityId"), null);
        if (sp != null) {
            result.add(sp);
        }
        return result;
    }
}
