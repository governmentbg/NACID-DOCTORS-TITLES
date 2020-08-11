package com.ext.nacid.web.taglib.applications.report.base;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.applications.report.base.ApplicationForReportBaseWebModel;
import com.nacid.web.model.applications.report.base.TrainingCourseForReportBaseWebModel;


public class ApplicationReportTrainingCourseBaseViewTag extends SimpleTagSupport {
	protected String attributePrefix;
	public void setAttributePrefix(String attributePrefix) {
		this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
	}
	protected void generateBaseData() {
		ApplicationForReportBaseWebModel webmodel = (ApplicationForReportBaseWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
		TrainingCourseForReportBaseWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
		/**
		 * zapishe li se nov zapis za application 100% se syzdava i nov zapis za
		 * TrainingCourse, taka 4e ne bi trqbvalo da ima situacii kogato tova neshto
		 * da e null!!!!
		 */
		if (trainingCourseWebModel == null) {
			return;
		}

		getJspContext().setAttribute("diploma_series", trainingCourseWebModel.getDiplomaSeries());
		getJspContext().setAttribute("diploma_number", trainingCourseWebModel.getDiplomaNumber());
		getJspContext().setAttribute("diploma_registration_number", trainingCourseWebModel.getDiplomaRegistrationNumber());
		getJspContext().setAttribute("diploma_date", trainingCourseWebModel.getDiplomaDate());

		//getJspContext().setAttribute("training_location_city", trainingCourseWebModel.getTrainingLocationCity());
		//getJspContext().setAttribute("training_location_country", trainingCourseWebModel.getTrainingLocationCountry());
		getJspContext().setAttribute("speciality", trainingCourseWebModel.getSpeciality());
		getJspContext().setAttribute("training_start", trainingCourseWebModel.getTrainingStart());
		getJspContext().setAttribute("training_end", trainingCourseWebModel.getTrainingEnd());
		getJspContext().setAttribute("training_duration", trainingCourseWebModel.getTrainingDuration());
		getJspContext().setAttribute("credits", trainingCourseWebModel.getCredits());
		getJspContext().setAttribute("edu_level_name", trainingCourseWebModel.getEducationLevelName());
		getJspContext().setAttribute("qualification", trainingCourseWebModel.getQualification());
		getJspContext().setAttribute("qualification", trainingCourseWebModel.getQualification());
		getJspContext().setAttribute("is_school_diploma_preset", trainingCourseWebModel.isSchoolDiplomaPreset());
		getJspContext().setAttribute("schoolCountry", trainingCourseWebModel.getSchoolCountry());
		getJspContext().setAttribute("schoolCity", trainingCourseWebModel.getSchoolCity());
		getJspContext().setAttribute("schoolName", trainingCourseWebModel.getSchoolName());
		getJspContext().setAttribute("schoolGraduationDate", trainingCourseWebModel.getSchoolGraduationDate());
		getJspContext().setAttribute("schoolNotes", trainingCourseWebModel.getSchoolNotes());
		getJspContext().setAttribute("training_form", trainingCourseWebModel.getTrainingForm());
		getJspContext().setAttribute("graduation_ways", trainingCourseWebModel.getGraduationWays());
		getJspContext().setAttribute("thesisTopic", trainingCourseWebModel.getThesisTopic());
		getJspContext().setAttribute("thesisTopicEn", trainingCourseWebModel.getThesisTopicEn());
		getJspContext().setAttribute("thesisAnnotation", trainingCourseWebModel.getThesisAnnotation());
		getJspContext().setAttribute("thesisAnnotationEn", trainingCourseWebModel.getThesisAnnotationEn());
		getJspContext().setAttribute("thesisBibliography", trainingCourseWebModel.getThesisBibliography());
		getJspContext().setAttribute("thesisDefenceDate", trainingCourseWebModel.getThesisDefenceDate());
		getJspContext().setAttribute("thesisLanguage", trainingCourseWebModel.getThesisLanguage());
		getJspContext().setAttribute("thesisVolume", trainingCourseWebModel.getThesisVolume());
		getJspContext().setAttribute("profGroupName", trainingCourseWebModel.getProfGroupName());
		getJspContext().setAttribute("eduAreaName", trainingCourseWebModel.getEduAreaName());

		//getJspContext().setAttribute("uni_validity_palce_recognized", trainingCourseWebModel.isRecognized());
		
		
		//getJspContext().setAttribute("training_institution", trainingCourseWebModel.getTrainingInstitution());
		
		
		UniversityWebModel prevDiplomaUniversity = trainingCourseWebModel.getPrevDiplUniversity();
		getJspContext().setAttribute("is_previous_bg_diploma_preset", !prevDiplomaUniversity.isNew());
		if (prevDiplomaUniversity != null) {
			getJspContext().setAttribute("prevDiplUniversityName", prevDiplomaUniversity.getBgName());
			getJspContext().setAttribute("prevDiplCountry", prevDiplomaUniversity.getCountry());
			getJspContext().setAttribute("prevDiplCity", prevDiplomaUniversity.getCity());
			getJspContext().setAttribute("prevDiplEduLevel", trainingCourseWebModel.getPrevDiplomaEduLevel());
			getJspContext().setAttribute("prevDiplGraduationDate", trainingCourseWebModel.getPrevDiplGraduationDate());
			getJspContext().setAttribute("prevDiplNotes", trainingCourseWebModel.getPrevDiplNotes());
			getJspContext().setAttribute("prevDiplSpeciality", trainingCourseWebModel.getPrevDiplomaSpeciality());
		}
		
		getJspContext().setAttribute("universities", trainingCourseWebModel.getUniversities());
		/*UniversityWebModel baseUniversityWebModel = trainingCourseWebModel.getBaseUniversity();
		if (baseUniversityWebModel != null) {
			getJspContext().setAttribute("baseUniversityBgName", baseUniversityWebModel.getBgName());
			getJspContext().setAttribute("baseUniversityOriginalName", baseUniversityWebModel.getOrgName());
			getJspContext().setAttribute("baseUniversityCountry", baseUniversityWebModel.getCountry());
			getJspContext().setAttribute("baseUniversityCity", baseUniversityWebModel.getCityName());
			getJspContext().setAttribute("baseUniversityAddress", baseUniversityWebModel.getAddrDetails());
		}*/
	}

}
