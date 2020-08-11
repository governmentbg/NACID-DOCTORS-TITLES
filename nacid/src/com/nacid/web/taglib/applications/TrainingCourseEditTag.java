package com.nacid.web.taglib.applications;

import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.applications.DiplomaTypeWebModel;
import com.nacid.web.model.applications.TrainingCourseWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;

public class TrainingCourseEditTag extends SimpleTagSupport {
	TrainingCourseWebModel trainingCourseWebModel;
	public static final String ARCHIVE_NUMBER_PREFIX = "ИД-05-01-";
	public void doTag() throws JspException, IOException {
		ApplicationWebModel webmodel = (ApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
		trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
		/**
		 * zapishe li se nov zapis za application 100% se syzdava i nov zapis za
		 * TrainingCourse, taka 4e ne bi trqbvalo da ima situacii kogato tova neshto
		 * da e null!!!!
		 */
		if (trainingCourseWebModel == null) {
			return;
		}


		getJspContext().setAttribute("application_id", webmodel.getId());
		getJspContext().setAttribute("application_header", webmodel.getApplicationHeader());
		getJspContext().setAttribute("application_type", webmodel.getApplicationType());
		getJspContext().setAttribute("training_course_id", trainingCourseWebModel.getId());
		getJspContext().setAttribute("training_duration", trainingCourseWebModel.getTrainingDuration());
		getJspContext().setAttribute("credits", trainingCourseWebModel.getCredits());
		getJspContext().setAttribute("diploma_series", trainingCourseWebModel.getDiplomaSeries());
		getJspContext().setAttribute("diploma_number", trainingCourseWebModel.getDiplomaNumber());
		getJspContext().setAttribute("diploma_registration_number", trainingCourseWebModel.getDiplomaRegistrationNumber());
		getJspContext().setAttribute("diploma_date", trainingCourseWebModel.getDiplomaDate());
		getJspContext().setAttribute("training_start", trainingCourseWebModel.getTrainingStart());
		getJspContext().setAttribute("training_end", trainingCourseWebModel.getTrainingEnd());
		getJspContext().setAttribute("has_education_period_information", trainingCourseWebModel.hasEducationPeriodInformation());
		getJspContext().setAttribute("edu_level_name", trainingCourseWebModel.getEducationLevelName());

		//getJspContext().setAttribute("joint_universities_size", trainingCourseWebModel.getJointUniversities() == null ? 0 : trainingCourseWebModel.getJointUniversities().size());
		getJspContext().setAttribute("training_locations_size", trainingCourseWebModel.getTrainingLocations() == null ? 1 : trainingCourseWebModel.getTrainingLocations().size());
		getJspContext().setAttribute("specialities_count", trainingCourseWebModel.getTrainingSpecialities() == null ? 0 : trainingCourseWebModel.getTrainingSpecialities().size());

		getJspContext().setAttribute("application_experts_size", webmodel.getApplicationExperts() == null ? 0 : webmodel.getApplicationExperts().size());
		getJspContext().setAttribute("archiveNumber", StringUtils.isEmpty(webmodel.getArchiveNumber()) ? ARCHIVE_NUMBER_PREFIX : webmodel.getArchiveNumber());

		getJspContext().setAttribute("archiveNumberContainerStyle", StringUtils.isEmpty(webmodel.getArchiveNumber()) ? "style=\"display:none;\"" : "");
		getJspContext().setAttribute("archiveNumberDisabled", webmodel.showArchiveNumber() ? "" : "disabled=\"disabled\"");

		getJspContext().setAttribute("schoolCity", stringToParameter(trainingCourseWebModel.getSchoolCity()));
		getJspContext().setAttribute("schoolName", stringToParameter(trainingCourseWebModel.getSchoolName()));
		getJspContext().setAttribute("schoolGraduationDate", stringToParameter(trainingCourseWebModel.getSchoolGraduationDate()));
		getJspContext().setAttribute("schoolNotes", stringToParameter(trainingCourseWebModel.getSchoolNotes()));
		getJspContext().setAttribute("prevDiplGraduationDate", stringToParameter(trainingCourseWebModel.getPrevDiplGraduationDate()));
		getJspContext().setAttribute("prevDiplNotes", stringToParameter(trainingCourseWebModel.getPrevDiplNotes()));
		
		getJspContext().setAttribute("universitiesCount", trainingCourseWebModel.getUniversitiesCount());
		getJspContext().setAttribute("trainingLocationsCount", trainingCourseWebModel.getTrainingLocations() == null ? 0 : trainingCourseWebModel.getTrainingLocations().size());
		getJspContext().setAttribute("notes", webmodel.getNotes());
		
		//getJspContext().setAttribute("trainingSpeciality", trainingCourseWebModel.getTrainingSpeciality());
		//getJspContext().setAttribute("trainingSpecialityId", trainingCourseWebModel.getTrainingSpecialityId());
		//RayaWritten-----------------------------------------------------------------------------------------------
		List<Speciality> trainingSpecialities = trainingCourseWebModel.getTrainingSpecialities();
        List<FlatNomenclature> originalSpecialities = trainingCourseWebModel.getTrainingOriginalSpecialities();
		if (trainingSpecialities != null) {
    		if(trainingSpecialities.size() == 1){
    		    FlatNomenclature originalSpeciality = originalSpecialities.get(0);
                getJspContext().setAttribute("trainingSingleSpecialityId", trainingSpecialities.get(0).getId());
    		    getJspContext().setAttribute("trainingSingleSpeciality", trainingSpecialities.get(0).getName());
                getJspContext().setAttribute("originalTrainingSingleSpecialityId", originalSpeciality == null ? "" : originalSpeciality.getId());
                getJspContext().setAttribute("originalTrainingSingleSpeciality", originalSpeciality == null ? "" : originalSpeciality.getName());

    		} else if(trainingSpecialities.size() > 1){
    		    getJspContext().setAttribute("trainingSpecialities", trainingSpecialities);
    		    getJspContext().setAttribute("trainingOriginalSpecialities", trainingCourseWebModel.getTrainingOriginalSpecialities());
    		}
		}
		//-----------------------------------------------------------------------------------------------------
		getJspContext().setAttribute("trainingQualification", trainingCourseWebModel.getTrainingQualification());
		getJspContext().setAttribute("trainingOriginalQualification", trainingCourseWebModel.getTrainingOriginalQualification());
		getJspContext().setAttribute("trainingQualificationId", trainingCourseWebModel.getTrainingQualificationId());
		getJspContext().setAttribute("trainingOriginalQualificationId", trainingCourseWebModel.getTrainingOriginalQualificationId());
		getJspContext().setAttribute("prevDiplomaSpeciality", trainingCourseWebModel.getPrevDiplomaSpeciality());
		getJspContext().setAttribute("prevDiplomaSpecialityId", trainingCourseWebModel.getPrevDiplomaSpecialityId());
		getJspContext().setAttribute("graduationDocumentTypeId", trainingCourseWebModel.getGraduationDocumentTypeId());
		getJspContext().setAttribute("creditHours", trainingCourseWebModel.getCreditHours());
		getJspContext().setAttribute("ectsCredits", trainingCourseWebModel.getEctsCredits());
		getJspContext().setAttribute("thesisTopic", trainingCourseWebModel.getThesisTopic());
		getJspContext().setAttribute("thesisTopicEn", trainingCourseWebModel.getThesisTopicEn());
		getJspContext().setAttribute("thesisAnnotation", trainingCourseWebModel.getThesisAnnotation());
		getJspContext().setAttribute("thesisAnnotationEn", trainingCourseWebModel.getThesisAnnotationEn());
		getJspContext().setAttribute("thesisBibliography", trainingCourseWebModel.getThesisBibliography());
		getJspContext().setAttribute("thesisDefenceDate", trainingCourseWebModel.getThesisDefenceDate());
		getJspContext().setAttribute("thesisVolume", trainingCourseWebModel.getThesisVolume());
		getJspContext().setAttribute("recognizedQualificationId", trainingCourseWebModel.getRecognizedQualificationId());
		getJspContext().setAttribute("recognizedQualification", trainingCourseWebModel.getRecognizedQualification());
		getJspContext().setAttribute("recognSpecs", trainingCourseWebModel.getRecognSpecs());

		getJspContext().setAttribute("submittedDocs", webmodel.getSubmittedDocs());
		DiplomaTypeWebModel dtwm = trainingCourseWebModel.getDiplomaTypeWebModel();
		if (dtwm != null) {
			getJspContext().setAttribute("edu_level_name", dtwm.getEducationLevel());
			getJspContext().setAttribute("original_edu_level_name", dtwm.getOriginalEducationLevel());
			getJspContext().setAttribute("original_edu_level_name_translated", dtwm.getOriginalEducationLevelTranslated());

		}

		//getJspContext().setAttribute("submittedDocsContainerStyle", StringUtils.isEmpty(webmodel.getArchiveNumber()) ? "style=\"display:none;\"" : "");
		//getJspContext().setAttribute("submittedDocsDisabled", webmodel.showSubmittedDocs() ? "" : "disabled=\"disabled\"");
		
		getJspBody().invoke(null);
	}

	public TrainingCourseWebModel getTrainingCourseWebModel() {
		return trainingCourseWebModel;
	}

	private static String stringToParameter(String s) {
		return s == null ? "" : s;
	}
}
