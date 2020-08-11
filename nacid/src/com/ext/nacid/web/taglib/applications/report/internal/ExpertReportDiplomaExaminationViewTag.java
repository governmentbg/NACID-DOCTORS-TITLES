package com.ext.nacid.web.taglib.applications.report.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.applications.report.internal.ApplicationInternalForReportWebModel;
import com.nacid.web.model.applications.report.internal.DiplomaExaminationInternalForReportWebModel;

public class ExpertReportDiplomaExaminationViewTag extends SimpleTagSupport {
    public void doTag() throws JspException, IOException {
        ApplicationInternalForReportWebModel applicationWebModel = (ApplicationInternalForReportWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        DiplomaExaminationInternalForReportWebModel diplomaExaminationWebModel = (DiplomaExaminationInternalForReportWebModel) getJspContext().getAttribute(WebKeys.DIPL_EXAM_WEB_MODEL, PageContext.REQUEST_SCOPE);
        
        if (applicationWebModel == null || diplomaExaminationWebModel == null) {
        	return;
        }
        
        if (diplomaExaminationWebModel != null) {
            getJspContext().setAttribute("examination_date", diplomaExaminationWebModel.getExaminationDate());
            getJspContext().setAttribute("notes", diplomaExaminationWebModel.getNotes());
            getJspContext().setAttribute("recognized", diplomaExaminationWebModel.isRecognized());
            getJspContext().setAttribute("universityCommunicated", diplomaExaminationWebModel.isUniversityCommunicated());
            getJspContext().setAttribute("institutionCommunicated", diplomaExaminationWebModel.isInstitutionCommunicated());
            getJspContext().setAttribute("foundInRegister", diplomaExaminationWebModel.isFoundInRegister());
            getJspContext().setAttribute("show_diploma_examination_attached_docs", getJspContext().getAttribute(WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL, PageContext.REQUEST_SCOPE) != null);
            //Ako nqma kompetentna instituciq, ne pokazva nishto, ako ima i tq nqma url - pokazva samo imeto j - ako ima i tq e s url - slaga link
            if (!StringUtils.isEmpty(diplomaExaminationWebModel.getCompetentInstitution())) {
            	String competentInstitution = diplomaExaminationWebModel.getCompetentInstitution();
            	if (!StringUtils.isEmpty(diplomaExaminationWebModel.getCompetentInstitutionUrl())) {
                	competentInstitution = "<a target=\"_blank\" href=\"" + diplomaExaminationWebModel.getCompetentInstitutionUrl() + "\">" + competentInstitution + "</a>";
                }
            	getJspContext().setAttribute("competent_institution", competentInstitution);
            } else {
            	getJspContext().setAttribute("competent_institution", null);
            }
            List<? extends UniversityWebModel> universitiesWebModel = applicationWebModel.getTrainingCourseWebModel().getUniversities();
            if (universitiesWebModel != null) {
            	List<String> universityNames = new ArrayList<String>();
            	for (UniversityWebModel u:universitiesWebModel) {
            		String uniName = u.getBgName();
                    if (u.getUrlDiplomaRegister() != null && !u.getUrlDiplomaRegister().equals("")) {
                        uniName = "<a target=\"_blank\" href=\"" + u.getUrlDiplomaRegister() + "\">" + uniName + "</a>";
                    }
                    universityNames.add(uniName);
            	}
            	getJspContext().setAttribute("diploma_examination_universities", universityNames);
            }
            
           
            getJspBody().invoke(null);
        }


    }

}
