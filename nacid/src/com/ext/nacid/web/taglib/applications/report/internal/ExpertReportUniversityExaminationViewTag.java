package com.ext.nacid.web.taglib.applications.report.internal;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.internal.UniversityExaminationInternalForReportWebModel;

public class ExpertReportUniversityExaminationViewTag extends SimpleTagSupport {
	public void doTag() throws JspException, IOException {
		List<UniversityExaminationInternalForReportWebModel> universityExaminations = (List<UniversityExaminationInternalForReportWebModel>) getJspContext().getAttribute(WebKeys.UNIVERSITY_EXAMINATION, PageContext.REQUEST_SCOPE);
		
		
		if (universityExaminations != null) {
			for (UniversityExaminationInternalForReportWebModel universityExamination:universityExaminations) {
				getJspContext().setAttribute("uni_bg_name", universityExamination.getUniBgName());
				getJspContext().setAttribute("uni_original_name", universityExamination.getUniOriginalName());
				getJspContext().setAttribute("is_examinated", universityExamination.isExaminated());
				getJspContext().setAttribute("examinationDate", universityExamination.getExaminationDate());
				getJspContext().setAttribute("examinationDate", universityExamination.getExaminationDate());
				
				getJspContext().setAttribute("is_communicated", universityExamination.isCommunicated());
				getJspContext().setAttribute("is_recognized", universityExamination.isRecognized());
				getJspContext().setAttribute("training_location", universityExamination.getTrainingLocation());
				getJspContext().setAttribute("training_form", universityExamination.getTrainingForm());
				getJspContext().setAttribute("competentInstitutions", universityExamination.getCompetentInstitutions());
				getJspContext().setAttribute("attachments", universityExamination.getAttachments());
				
				getJspContext().setAttribute("notes", universityExamination.getNotes());
				getJspContext().setAttribute("universityValidityNotes", universityExamination.getUniversityValidatyNotes());
				
				//getJspContext().setAttribute("show_univalidity_attachments", universityExamination.getAttachments() != null);			
				getJspBody().invoke(null);	
			}
				
			
			
		}
		
		
	}
	
}
