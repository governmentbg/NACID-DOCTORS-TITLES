package com.ext.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.model.applications.ExtTrainingCourseSpecialityWebModel;
import com.ext.nacid.web.model.applications.ExtTrainingCourseWebModel;
import com.nacid.bl.nomenclatures.EducationLevel;
import com.nacid.web.WebKeys;


public class ExtTrainingCourseEditTag extends SimpleTagSupport {
    
    @Override
    public void doTag() throws JspException, IOException {
        
        ExtTrainingCourseWebModel webModel = (ExtTrainingCourseWebModel) getJspContext().getAttribute(WebKeys.EXT_TRAINING_COURSE_WEB_MODEL, PageContext.REQUEST_SCOPE);
        
        getJspContext().setAttribute("applicationId", webModel.getApplicationId());
        getJspContext().setAttribute("universities", webModel.getUniversities());
        getJspContext().setAttribute("diplomaSeries", webModel.getDiplomaSeries());
        getJspContext().setAttribute("diplomaNumber", webModel.getDiplomaNumber());
        getJspContext().setAttribute("diplomaRegistrationNumber", webModel.getDiplomaRegistrationNumber());
        getJspContext().setAttribute("diplomaDate", webModel.getDiplomaDate());
        getJspContext().setAttribute("trainingStart", webModel.getTrainingStart());
        getJspContext().setAttribute("trainingEnd", webModel.getTrainingEnd());
        getJspContext().setAttribute("credits", webModel.getCredits());
        getJspContext().setAttribute("schoolCity", webModel.getSchoolCity());
        getJspContext().setAttribute("schoolName", webModel.getSchoolName());
        getJspContext().setAttribute("schoolGraduationDate", webModel.getSchoolGraduationDate());
        getJspContext().setAttribute("schoolNotes", webModel.getSchoolNotes());
        getJspContext().setAttribute("prevDiplName", webModel.getPrevDiplName());
        getJspContext().setAttribute("prevDiplUniversityId", webModel.getPrevDiplUniversityId());
        getJspContext().setAttribute("prevDiplGraduationDate", webModel.getPrevDiplGraduationDate());
        getJspContext().setAttribute("prevDiplNotes", webModel.getPrevDiplNotes());
        getJspContext().setAttribute("prevDiplomaSpecialityId", webModel.getPrevDiplomaSpecialityId());
        getJspContext().setAttribute("prevDiplomaSpeciality", webModel.getPrevDiplomaSpecialityName());
        getJspContext().setAttribute("trainingDuration", webModel.getTrainingDuration());
        getJspContext().setAttribute("qualificationId", webModel.getQualificationId());
        getJspContext().setAttribute("qualification", webModel.getQualificationName());
        getJspContext().setAttribute("thesisTopic", webModel.getThesisTopic());
        getJspContext().setAttribute("thesisTopicEn", webModel.getThesisTopicEn());
        getJspContext().setAttribute("thesisAnnotation", webModel.getThesisAnnotation());
        getJspContext().setAttribute("thesisAnnotationEn", webModel.getThesisAnnotationEn());
        getJspContext().setAttribute("thesisBibliography", webModel.getThesisBibliography());
        getJspContext().setAttribute("thesisDefenceDate", webModel.getThesisDefenceDate());
        getJspContext().setAttribute("thesisVolume", webModel.getThesisVolume());
        getJspContext().setAttribute("hasDissertationGraduationWay", webModel.isHasDissertationGraduationWay());
        //
        
        if (webModel.getTrainingSpecialities() != null) {
            if (webModel.getTrainingSpecialities().size() > 1) {
                getJspContext().setAttribute("specialitiesList", webModel.getTrainingSpecialities());
            } else {
                ExtTrainingCourseSpecialityWebModel s = webModel.getTrainingSpecialities().get(0);
                getJspContext().setAttribute("trainingSpecialityId", s.getSpecialityId());
                getJspContext().setAttribute("trainingSpecialityTxt", s.getSpecialityName());
            }

        }
       
        getJspContext().setAttribute("training_locations_size", webModel.getTrainingLocations() == null ? 1 : webModel.getTrainingLocations().size());
        getJspContext().setAttribute("universities_count", webModel.getUniversities() == null ? 1 : webModel.getUniversities().size());
        getJspContext().setAttribute("specialities_count", webModel.getTrainingSpecialities() == null ? 0 : webModel.getTrainingSpecialities().size());
        getJspContext().setAttribute("has_education_period_information", webModel.hasEducationPeriodInformation());
        getJspContext().setAttribute("doctorOfScienceId", EducationLevel.EDUCATION_LEVEL_DOCTOR_OF_SCIENCE);
        getJspBody().invoke(null);
    }
}
