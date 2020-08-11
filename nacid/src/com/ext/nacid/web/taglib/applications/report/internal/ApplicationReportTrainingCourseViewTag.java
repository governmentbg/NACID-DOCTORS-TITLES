package com.ext.nacid.web.taglib.applications.report.internal;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.ext.nacid.web.taglib.applications.report.base.ApplicationReportTrainingCourseBaseViewTag;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.internal.ApplicationInternalForReportWebModel;
import com.nacid.web.model.applications.report.internal.DiplomaTypeInternalForReportWebModel;
import com.nacid.web.model.applications.report.internal.TrainingCourseInternalForReportWebModel;

//import com.sun.accessibility.internal.resources.accessibility;

public class ApplicationReportTrainingCourseViewTag extends ApplicationReportTrainingCourseBaseViewTag {
	
	public void doTag() throws JspException, IOException {
		super.generateBaseData();
		
		ApplicationInternalForReportWebModel webmodel = (ApplicationInternalForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
		TrainingCourseInternalForReportWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
		/**
		 * zapishe li se nov zapis za application 100% se syzdava i nov zapis za
		 * TrainingCourse, taka 4e ne bi trqbvalo da ima situacii kogato tova neshto
		 * da e null!!!!
		 */
		if (trainingCourseWebModel == null) {
			return;
		}
		//TODO:Da dobavq informaciq za trainingInstitutions
		String diplomaType = "";
		DiplomaTypeInternalForReportWebModel diplomaTypeWebModel = trainingCourseWebModel.getDiplomaTypeWebModel();
		if (diplomaTypeWebModel != null) {
			diplomaType += diplomaTypeWebModel.getTitle() + "/" + diplomaTypeWebModel.getEducationLevel();
		}
		getJspContext().setAttribute("diploma_type", diplomaType);
		
		
		getJspBody().invoke(null);
	}

}
