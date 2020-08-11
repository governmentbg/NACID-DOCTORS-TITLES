package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.comission.ApplicationMotivesHandler;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ApplicationsStatusHandler extends NacidBaseRequestHandler {
    
    private final static String UNIVERSITY_VALIDITY_MSG_ID = "universityValidityMsg";
    private final static String APPLICATION_STATUS_MSG_ID = "applicationStatusMsg";
    private final static String DIPLOMA_EXAM_MSG_ID = "diplomaExamMsg";
    private final static String UNIVERSITY_VALIDITY_BY_PLACE_MSG_ID = "universityValidityByPlaceMsg";

    private ServletContext servletContext;
    public ApplicationsStatusHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("application_id"), -1);
        Application application = applicationId == -1 ? null : getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
        if (application == null) {
            throw Utils.logException(new Exception("No applicaiton ID is set...."));
        }
        request.setAttribute(WebKeys.APPLICATION_ID, applicationId);
        String type = request.getParameter("type");
        if ("application_status".equals(type)) {
            saveApplicationStatus(request, response, application);
        } else if ("university".equals(type)) {
            saveUniversityValidity(request, response, application);
        } else if ("diploma_examination".equals(type)) {
            saveDiplomaExamination(request, response, application);
        } else if ("universityPlace".equals(type)) {
            saveUniversityValidityByPlace(request, response, application);
        } else if ("application_expert".equals(type)) {
            // saveApplictionExpert(request, response, application);
            throw new RuntimeException("Error Error Error");
        } else {
            throw Utils.logException(new Exception("No operation defined..."));
        }
        //Deleting applications table from session
        ApplicationsHandler.removeTableSessionAttributes(request);
        
        String redirectString =  servletContext.getAttribute("pathPrefix") 
                + "/control/applications/edit?activeForm=4&id=" + applicationId;
        try {
            response.sendRedirect(redirectString);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    private void saveUniversityValidityByPlace(HttpServletRequest request, HttpServletResponse response, Application application) {
        
        int tcId = DataConverter.parseInt(request.getParameter("trainingCourseStatusId"), -1);
        boolean recognized = DataConverter.parseBoolean(
                request.getParameter("university_validity_place_recognized"));
        /*boolean hasUniInstitution = DataConverter.parseBoolean(
                request.getParameter("hasUniInstitution"));
        Integer trainingInstId = null;
        if(hasUniInstitution) {
            trainingInstId = DataConverter.parseInteger(
                    request.getParameter("uniInstitution"), null);
        }*/
                
        
        TrainingCourseDataProvider tcDP = getNacidDataProvider().getTrainingCourseDataProvider();
        
        TrainingCourse tc = tcDP.getTrainingCourse(tcId);
        List<? extends TrainingCourseTrainingLocation> trainingLocations = tc.getTrainingCourseTrainingLocations();
        if (trainingLocations != null) {
        	for (TrainingCourseTrainingLocation tl:trainingLocations) {
				boolean hasInstitutionId = DataConverter.parseBoolean(request.getParameter("hasUniInstitution" + tl.getId()));
        		Integer trainingInstitution = hasInstitutionId ? DataConverter.parseInteger(request.getParameter("uniInstitution" + tl.getId()), null) : null;
        		tcDP.saveTrainingCourseTrainingLocation(tl.getId(), tcId, tl.getTrainingLocationCountryId(), tl.getTrainingLocationCity(), trainingInstitution);
        	}
        }
        
        tcDP.saveTrainingCourse(tc.getId(), tc.getDiplomaSeries(), tc.getDiplomaNumber(), tc.getDiplomaRegistrationNumber(), tc.getDiplomaDate(),
                tc.getDiplomaTypeId(), tc.getFName(), tc.getSName(), 
                tc.getLName(), tc.isJointDegree(), 
                tc.getTrainingStart(), 
                tc.getTrainingEnd(), tc.getTrainingDuration(), 
                tc.getDurationUnitId(), 
                tc.getCredits(),
                tc.getEducationLevelId(), tc.getQualificationId(), recognized,
                tc.getSchoolCountryId(), tc.getSchoolCity(),
                tc.getSchoolName(), tc.getSchoolGraduationDate(), tc.getSchoolNotes(), 
                tc.getPrevDiplomaUniversityId() , tc.getPrevDiplomaEduLevelId(),
                tc.getPrevDiplomaGraduationDate(), tc.getPrevDiplomaNotes(), tc.getPrevDiplomaSpecialityId(), 
                tc.getRecognizedEduLevelId(), /*trainingInstId,*/
                tc.getRecognizedQualificationId(),
                tc.getGraduationDocumentTypeId(), tc.getCreditHours(), tc.getEctsCredits(),
                tc.getOwnerId(),
                tc.getThesisTopic(),
                tc.getThesisTopicEn(), tc.getProfGroupId(), tc.getRecognizedProfGroupId(), tc.getThesisDefenceDate(), tc.getThesisBibliography(), tc.getThesisVolume(), tc.getThesisLanguageId(), tc.getThesisAnnotation(), tc.getThesisAnnotationEn(), tc.getOriginalQualificationId());
        
        addSystemMessageToSession(request, UNIVERSITY_VALIDITY_BY_PLACE_MSG_ID, 
                new SystemMessageWebModel("Данните бяха записани в базата.", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
       
        
        autoStatusChange(request, response, application.getId(), 
                ApplicationStatus.APPLICATION_LEGITIMNO_PO_MIASTO_STATUS_CODE);
    }
    
    private void saveApplicationStatus(HttpServletRequest request, HttpServletResponse response, Application application) {
        int applicationStatusId = DataConverter.parseInt(request.getParameter("application_status"), -1);
        int applicationDocflowStatusId = DataConverter.parseInt(request.getParameter("application_docflow_status"), -1);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        if (applicationStatusId != -1 && applicationDocflowStatusId != -1) {
        	ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        	/*if (applicationStatusId == ApplicationStatus.APPLICATION_ARCHIVED_STATUS_CODE) {
        		List<AppStatusHistory> statusHistory = applicationsDataProvider.getAppStatusHistory(application.getId());
        		
        		if (statusHistory == null || 
        				(statusHistory.get(0).getApplicationStatusId() != ApplicationStatus.APPLICATION_ARCHIVED_STATUS_CODE &&
        				statusHistory.get(0).getApplicationStatusId() != ApplicationStatus.APPLICATION_FINISHED_STATUS_CODE)) {
        			NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
        			FlatNomenclature archivedStatus = nomenclaturesDataProvider.getApplicationStatus(NacidDataProvider.APP_NACID_ID, ApplicationStatus.APPLICATION_ARCHIVED_STATUS_CODE);
        			FlatNomenclature finishedStatus = nomenclaturesDataProvider.getApplicationStatus(NacidDataProvider.APP_NACID_ID, ApplicationStatus.APPLICATION_FINISHED_STATUS_CODE);
        			
        			addSystemMessageToSession(request, APPLICATION_STATUS_MSG_ID, 
                            new SystemMessageWebModel("За да промените статуса на заявление в "+ archivedStatus.getName() + ", трябва преди това завлението да е било със статус " + finishedStatus.getName(), SystemMessageWebModel.MESSAGE_TYPE_ERROR));
        			return;
        		}
        	}*/

            Integer recognizedEduLevel = DataConverter.parseInteger(request.getParameter("recognizedEduLevel"), null);
            Integer recognizedProfGroupId = DataConverter.parseInteger(request.getParameter("recognizedProfGroup"), null);
            Integer recognizedQualification = DataConverter.parseInteger(request.getParameter("recognizedQualificationId"), null);
            Set<Integer> recognizedSpecialities = ApplicationMotivesHandler.getRecognizedSpecialitiesFromRequest(request);



            TrainingCourseDataProvider tcdp = nacidDataProvider.getTrainingCourseDataProvider();

        	String archiveNumber = applicationDocflowStatusId == ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE ? request.getParameter("archive_number") : application.getArchiveNumber();
        	String submittedDocs = applicationStatusId == ApplicationStatus.APPLICATION_POSTPONED_SUBMITTED_DOCS ? request.getParameter("submitted_docs") : application.getSubmittedDocs();
        	Integer legalReason = DataConverter.parseInteger(request.getParameter("legalReason"), null);
        	applicationsDataProvider.updateApplicationStatus(application.getId(), applicationStatusId, applicationDocflowStatusId, archiveNumber, legalReason, submittedDocs, getLoggedUser(request, response).getUserId());




            addSystemMessageToSession(request, APPLICATION_STATUS_MSG_ID,
                    new SystemMessageWebModel("Статусът беше променен!", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));

            tcdp.updateRecognizedDetails(application.getTrainingCourseId(), recognizedEduLevel, recognizedQualification, recognizedProfGroupId, new ArrayList<>(recognizedSpecialities));

            return;
        } else {
            addSystemMessageToSession(request, APPLICATION_STATUS_MSG_ID, 
                    new SystemMessageWebModel("Непознат статус :" + request.getParameter("application_status"), SystemMessageWebModel.MESSAGE_TYPE_ERROR));
           
            return;
        }
        
    }
    private void saveUniversityValidity(HttpServletRequest request, HttpServletResponse response, Application application) {
        
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        TrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getTrainingCourseDataProvider();
        
        UniversityValidityDataProvider universityValidityDataProvider = nacidDataProvider.getUniversityValidityDataProvider();
        User user = getLoggedUser(request, response);
        int trainingCourseId = application.getTrainingCourseId();
        
        String notes = DataConverter.parseString(request.getParameter("university_examination_notes"), null);
        String[] universityValidityInputs = request.getParameterValues("university_validity");
        if (universityValidityInputs != null && universityValidityInputs.length > 0) {
            for (String s:universityValidityInputs) {
                Integer i = DataConverter.parseInteger(s, null);
                if (i != null) {
                    UniversityValidity uv = universityValidityDataProvider.getUniversityValidity(i);
                    if (uv == null) {
                        addSystemMessageToSession(request, UNIVERSITY_VALIDITY_MSG_ID, 
                                new SystemMessageWebModel("Непознат University Validity запис.... id = " + i, SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                       
                        return;
                    }
                    trainingCourseDataProvider.saveUniversityExamination(trainingCourseId, i, user.getUserId(), notes);;
                    addSystemMessageToSession(request, UNIVERSITY_VALIDITY_MSG_ID + uv.getUniversityId(),  new SystemMessageWebModel("Данните бяха записани в базата.", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
                    
                }
                
            }
            
        }
        
       
        
        autoStatusChange(request, response, application.getId(), 
                ApplicationStatus.APPLICATION_LEGITIMNO_PO_SEDALISHTE_STATUS_CODE);
    }
    
    private void saveDiplomaExamination(HttpServletRequest request, HttpServletResponse response, Application application) {
        TrainingCourseDataProvider trainingCourseDataProvider = getNacidDataProvider().getTrainingCourseDataProvider();
        Date examinationDate = DataConverter.parseDate(request.getParameter("diploma_examination_date"));
        TrainingCourse trainingCourse = application.getTrainingCourse();
        DiplomaExamination diplomaExamination = trainingCourse.getDiplomaExamination();
        int diplomaExaminationId = diplomaExamination == null ? 0 : diplomaExamination.getId();
        int trainingCourseId = trainingCourse.getId();
        int userId = diplomaExamination == null ? getLoggedUser(request, response).getUserId() : diplomaExamination.getUserId();
        String notes = request.getParameter("diploma_examination_notes");
        boolean isRecognized = DataConverter.parseBoolean(request.getParameter("diploma_examination_recognized"));
        boolean isInstitutionCommunicated = DataConverter.parseBoolean(request.getParameter("diploma_examination_institutionCommunicated"));
        boolean isUniversityCommunicated = DataConverter.parseBoolean(request.getParameter("diploma_examination_universityCommunicated"));
        boolean isFoundInRegister = DataConverter.parseBoolean(request.getParameter("diploma_examination_foundInRegister"));
        Integer competentInstitutionId = DataConverter.parseInteger(request.getParameter("diploma_examination_competent_institution"), null);
        trainingCourseDataProvider.saveDiplomaExamination(diplomaExaminationId, trainingCourseId, userId, examinationDate, notes, isRecognized, competentInstitutionId, isInstitutionCommunicated, isUniversityCommunicated, isFoundInRegister);
         
        addSystemMessageToSession(request, DIPLOMA_EXAM_MSG_ID, 
                new SystemMessageWebModel(MessagesBundle.getMessagesBundle().getValue("diploma_examination_changed"), SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
       
        
        autoStatusChange(request, response, application.getId(), 
                ApplicationStatus.APPLICATION_AUTHENTIC_STATUS_CODE);
    }
    
    private void autoStatusChange(HttpServletRequest request, HttpServletResponse response, int applicationId, int newStatus) {
        
        Application application = getNacidDataProvider().getApplicationsDataProvider()
                .getApplication(applicationId);
        TrainingCourse tc = application.getTrainingCourse();
        
        boolean universityExaminationIsRecognized = tc.isRecognizedByHeadQuarter();
        
        DiplomaExamination de = tc.getDiplomaExamination();
        
        boolean check = false;
        switch (newStatus) {
            case ApplicationStatus.APPLICATION_LEGITIMNO_PO_SEDALISHTE_STATUS_CODE:
                check = application.getApplicationStatusId() == ApplicationStatus.APPLICATION_FOR_EXAMINATION_STATUS_CODE
                    && universityExaminationIsRecognized;
                break;
            case ApplicationStatus.APPLICATION_LEGITIMNO_PO_MIASTO_STATUS_CODE:
                check = application.getApplicationStatusId() == ApplicationStatus.APPLICATION_LEGITIMNO_PO_SEDALISHTE_STATUS_CODE
                    && universityExaminationIsRecognized && tc.isRecognized();
                break;
            case ApplicationStatus.APPLICATION_AUTHENTIC_STATUS_CODE:
                check = application.getApplicationStatusId() == ApplicationStatus.APPLICATION_LEGITIMNO_PO_MIASTO_STATUS_CODE
                    && universityExaminationIsRecognized && tc.isRecognized() && de.isRecognized();
                break;
                
            default:
                throw Utils.logException(new Exception("Unknown new status....."));
        }
        
        if(check) {
            getNacidDataProvider().getApplicationsDataProvider()
                .updateApplicationStatus(applicationId, newStatus, application.getApplicationDocflowStatusId(), null, null, null, getLoggedUser(request, response).getUserId());
            
            addSystemMessageToSession(request, APPLICATION_STATUS_MSG_ID, 
                    new SystemMessageWebModel("Статусът беше автоматично променен.", 
                            SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        }
        else {
            addSystemMessageToSession(request, APPLICATION_STATUS_MSG_ID, 
                    new SystemMessageWebModel("Статусът не може да бъде променен автоматично.", 
                            SystemMessageWebModel.MESSAGE_TYPE_ERROR));
        }
    }
    
   

}
