package com.ext.nacid.web.taglib.applications.report.internal;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.internal.UniversityExaminationByPlaceInternalForReportWebModel;

public class ExpertReportUniversityExaminationByPlaceViewTag extends SimpleTagSupport {
	public void doTag() throws JspException, IOException {
		List<UniversityExaminationByPlaceInternalForReportWebModel> universityExaminations = (List<UniversityExaminationByPlaceInternalForReportWebModel>) getJspContext().getAttribute(WebKeys.UNIVERSITY_EXAMINATION_BY_PLACE_WEB_MODEL, PageContext.REQUEST_SCOPE);

		if (universityExaminations != null) {
			for (UniversityExaminationByPlaceInternalForReportWebModel universityExamination : universityExaminations) {
				getJspContext().setAttribute("trainingLocation", universityExamination.getTrainingLocation());
				getJspContext().setAttribute("trainingInstitution", universityExamination.getTrainingInstitution());

				getJspBody().invoke(null);
			}

		}

	}

}
