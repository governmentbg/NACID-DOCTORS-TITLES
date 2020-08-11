package com.ext.nacid.web.handlers.impl.applications;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.AppStatusHistory;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.applications.DiplomaExamination;
import com.nacid.bl.applications.TrainingCourse;
import com.nacid.bl.applications.TrainingCourseTrainingLocation;
import com.nacid.bl.applications.University;
import com.nacid.bl.impl.applications.TrainingCourseDataProviderImpl;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.AppStatusHistoryWebModel;
import com.nacid.web.model.applications.report.base.AttachmentForReportBaseWebModel;
import com.nacid.web.model.applications.report.internal.DiplomaExaminationInternalForReportWebModel;
import com.nacid.web.model.applications.report.internal.UniversityExaminationByPlaceInternalForReportWebModel;
import com.nacid.web.model.applications.report.internal.UniversityExaminationInternalForReportWebModel;

public class ExpertReportHandler extends NacidExtBaseRequestHandler {

	public ExpertReportHandler(ServletContext servletContext) {
		super(servletContext);
	}
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
		Integer id = DataConverter.parseInteger(request.getParameter("id"), null);
		if (id == null ) {
			throw new RuntimeException("no application id is set... id=" + request.getParameter("id"));
		}
		prepareExpertReport(request, response, getNacidDataProvider(), id);
		request.setAttribute(WebKeys.NEXT_SCREEN, "expert_application_view");
	}

	//Generira report ot zaqvlenie vyv vytre6noto prilojenie 
	public static void prepareExpertReport(HttpServletRequest request, HttpServletResponse response, NacidDataProvider nacidDataProvider, int internalApplicationId) {
		ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		Application internalApplication = internalApplicationId <= 0 ? null : applicationsDataProvider.getApplication(internalApplicationId);
		if (internalApplication == null) {
			throw new RuntimeException("no application with given id");
		}
		
		//generating application report
		ApplicantReportHandler.generateInternalApplicantReport(nacidDataProvider, internalApplication, request, null);
		//end of generating application report
		
		//generating statuses history table
		List<AppStatusHistoryWebModel> appStatusWMList = new ArrayList<AppStatusHistoryWebModel>();
		ApplicationsDataProvider appDP = nacidDataProvider.getApplicationsDataProvider();
		List<AppStatusHistory> appStatusHistoryList = appDP.getAppStatusHistory(internalApplication.getId());
		for (AppStatusHistory ash : appStatusHistoryList) {
			appStatusWMList.add(new AppStatusHistoryWebModel(ash));
		}
		request.setAttribute(WebKeys.STATUS_HISTORY_LIST_WEB_MODEL, appStatusWMList);
		//end of generating statuses history table
		
		//start of university examination generation
		TrainingCourse trainingCourse = internalApplication.getTrainingCourse();
		List<? extends UniversityWithFaculty> universities = trainingCourse.getUniversityWithFaculties();
		if (universities != null) {
			List<UniversityExaminationInternalForReportWebModel> webmodel = new ArrayList<UniversityExaminationInternalForReportWebModel>();
			for (UniversityWithFaculty u:universities) {
				webmodel.add(new UniversityExaminationInternalForReportWebModel(u.getUniversity(), trainingCourse.getUniversityExaminationByUniversity(u.getUniversity().getId()), nacidDataProvider));
			}
			request.setAttribute(WebKeys.UNIVERSITY_EXAMINATION, webmodel);
		}
		//end of university examination generation
		
		
		//start of university examination by place generation
		List<? extends TrainingCourseTrainingLocation> trainingLocations = trainingCourse.getTrainingCourseTrainingLocations();
		if (trainingLocations != null) {
			List<UniversityExaminationByPlaceInternalForReportWebModel> uniExamByPlaceWebModel = new ArrayList<UniversityExaminationByPlaceInternalForReportWebModel>();
			for (TrainingCourseTrainingLocation tl:trainingLocations) {
				uniExamByPlaceWebModel.add(new UniversityExaminationByPlaceInternalForReportWebModel(tl));
			}
			request.setAttribute(WebKeys.UNIVERSITY_EXAMINATION_BY_PLACE_WEB_MODEL, uniExamByPlaceWebModel);
		}
		//end of university examination by place generation		
		//diploma examination data validity
		DiplomaExamination diplomaExamination = trainingCourse.getDiplomaExamination();
		if (diplomaExamination != null) {
			request.setAttribute(WebKeys.DIPL_EXAM_WEB_MODEL, new DiplomaExaminationInternalForReportWebModel(diplomaExamination));
			AttachmentDataProvider diplExamAttachmentDataProvider = nacidDataProvider.getDiplExamAttachmentDataProvider();
			List<? extends Attachment> diplExamAttachedDocs = diplExamAttachmentDataProvider.getAttachmentsForParent(diplomaExamination.getId());
			if (diplExamAttachedDocs != null && diplExamAttachedDocs.size() > 0) {
				List<AttachmentForReportBaseWebModel> dipExamAttachedDocsWebModel = new ArrayList<AttachmentForReportBaseWebModel>();
				for (Attachment a: diplExamAttachedDocs) {
					dipExamAttachedDocsWebModel.add(new AttachmentForReportBaseWebModel(a));
				}
				request.setAttribute(WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL, dipExamAttachedDocsWebModel);
			}
		}
		//end of diploma examination data validity
		
		//setNextScreen(request, "report_expert");
	}
	public static void main(String[] args) {
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		TrainingCourseDataProviderImpl trainingCourseDataProvider = (TrainingCourseDataProviderImpl) nacidDataProvider.getTrainingCourseDataProvider();
		System.out.println(trainingCourseDataProvider.getDiplomaExamination(52));
	}

}
