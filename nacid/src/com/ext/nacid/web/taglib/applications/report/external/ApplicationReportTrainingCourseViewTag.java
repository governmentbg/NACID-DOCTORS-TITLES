package com.ext.nacid.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;

import com.ext.nacid.web.model.applications.report.ExtApplicationForReportWebModel;
import com.ext.nacid.web.model.applications.report.ExtTrainingCourseForReportWebModel;
import com.ext.nacid.web.taglib.applications.report.base.ApplicationReportTrainingCourseBaseViewTag;
import com.nacid.web.WebKeys;

public class ApplicationReportTrainingCourseViewTag extends ApplicationReportTrainingCourseBaseViewTag {
	
	public void doTag() throws JspException, IOException {
		super.generateBaseData();
		
		ExtApplicationForReportWebModel webmodel = (ExtApplicationForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
		ExtTrainingCourseForReportWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
		
		getJspContext().setAttribute("is_previous_bg_diploma_preset", !StringUtils.isEmpty(trainingCourseWebModel.getPrevDiplomaUniversityTxt()) || (trainingCourseWebModel.getPrevDiplUniversity() != null && !trainingCourseWebModel.getPrevDiplUniversity().isNew()));
		getJspContext().setAttribute("speciality_txt", trainingCourseWebModel.getSpecialityTxt());
		getJspContext().setAttribute("qualification_txt", trainingCourseWebModel.getQualificationTxt());
		getJspContext().setAttribute("prev_diploma_university_txt", trainingCourseWebModel.getPrevDiplomaUniversityTxt());
		getJspContext().setAttribute("prevDiplSpecialityTxt", trainingCourseWebModel.getPrevDiplomaSpecialityTxt());
		//getJspContext().setAttribute("training_institution_txt", trainingCourseWebModel.getTrainingInstitutionTxt());
		
		
		getJspBody().invoke(null);
	}

}
