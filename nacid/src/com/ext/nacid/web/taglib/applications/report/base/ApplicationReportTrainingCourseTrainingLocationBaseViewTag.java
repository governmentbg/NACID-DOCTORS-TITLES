package com.ext.nacid.web.taglib.applications.report.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.base.ApplicationForReportBaseWebModel;
import com.nacid.web.model.applications.report.base.TrainingCourseForReportBaseWebModel;
import com.nacid.web.model.applications.report.base.TrainingCourseTrainingLocationForReportBaseWebModel;


public class ApplicationReportTrainingCourseTrainingLocationBaseViewTag extends SimpleTagSupport {
	protected String attributePrefix;
	public void setAttributePrefix(String attributePrefix) {
		this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
	}
	
	public void doTag() throws JspException, IOException {
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
		List<TrainingCourseTrainingLocationForReportBaseWebModel> trainingLocations = trainingCourseWebModel.getTrainingLocations();
		if (trainingLocations != null) {
			for (TrainingCourseTrainingLocationForReportBaseWebModel m:trainingLocations) {
				getJspContext().setAttribute("training_location_city", m.getTrainingCity());
				getJspContext().setAttribute("training_location_country", m.getTrainingCountry());
				getJspBody().invoke(null);
			}
		}
	}
}
