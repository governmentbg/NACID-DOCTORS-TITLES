package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofTrainingCourseForReportWebModel;
import com.ext.nacid.regprof.web.taglib.applications.report.base.RegprofApplicationReportTrainingCourseBaseViewTag;
import com.nacid.web.WebKeys;
//RayaWritten------------------------------------------------------------------------------------------------
public class RegprofApplicationReportTrainingCourseViewTag extends RegprofApplicationReportTrainingCourseBaseViewTag{
    ExtRegprofApplicationForReportWebModel webmodel;
    @Override
    public void doTag() throws JspException, IOException {
        webmodel = (ExtRegprofApplicationForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        ExtRegprofTrainingCourseForReportWebModel trainingCourseWebModel = (ExtRegprofTrainingCourseForReportWebModel) webmodel.getTrainingCourseWebModel();
        
        getJspContext().setAttribute("prof_institution_name_txt", trainingCourseWebModel.getProfInstitutionNameTxt());
        getJspContext().setAttribute("prof_institution_org_name_txt", trainingCourseWebModel.getProfInstitutionOrgNameTxt());
        getJspContext().setAttribute("sdk_prof_institution_name_txt", trainingCourseWebModel.getSdkProfInstitutionNameTxt());
        getJspContext().setAttribute("sdk_prof_institution_org_name_txt", trainingCourseWebModel.getSdkProfInstitutionOrgNameTxt());
        
        getJspContext().setAttribute("high_prof_qualification_txt", trainingCourseWebModel.getHighProfQualificationTxt());
        getJspContext().setAttribute("sdk_prof_qualification_txt", trainingCourseWebModel.getSdkProfQualificationTxt());
        getJspContext().setAttribute("sec_prof_qualification_txt", trainingCourseWebModel.getSecProfQualificationTxt());
        getJspContext().setAttribute("certificate_prof_qual_txt", trainingCourseWebModel.getCertificateProfQualificationTxt());
        
        super.generateBaseData();
       
        getJspBody().invoke(null);
    }
    public ExtRegprofApplicationForReportWebModel getWebmodel() {
        return webmodel;
    }
    
}
//--------------------------------------------------------------------------------------------------------------
