package com.ext.nacid.regprof.web.taglib.applications.report.base;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.regprof.web.model.applications.report.base.RegprofApplicationForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseForReportBaseWebModel;
import com.nacid.web.WebKeys;

//RayaWritten---------------------------------------------------------------
public class RegprofApplicationReportTrainingCourseBaseViewTag extends SimpleTagSupport{
    protected String attributePrefix;
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }
    protected void generateBaseData() {
        RegprofApplicationForReportBaseWebModel webmodel = (RegprofApplicationForReportBaseWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        RegprofTrainingCourseForReportBaseWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
        /**
         * zapishe li se nov zapis za application 100% se syzdava i nov zapis za
         * TrainingCourse, taka 4e ne bi trqbvalo da ima situacii kogato tova neshto
         * da e null!!!!
         */
        if (trainingCourseWebModel == null) {
            return;
        }

        getJspContext().setAttribute("sdk", trainingCourseWebModel.isSdk());
        getJspContext().setAttribute("high", trainingCourseWebModel.isHigh());
        getJspContext().setAttribute("sec", trainingCourseWebModel.isSec());        

        getJspContext().setAttribute("prof_institution_name", trainingCourseWebModel.getProfInstitution());
        getJspContext().setAttribute("prof_institution_org_name", trainingCourseWebModel.getProfInstitutionOrgName());
        getJspContext().setAttribute("sdk_prof_institution_name", trainingCourseWebModel.getSdkProfInstitution());
        getJspContext().setAttribute("sdk_prof_institution_org_name", trainingCourseWebModel.getSdkProfInstitutionOrgName());
        
        getJspContext().setAttribute("high_prof_qualification", trainingCourseWebModel.getHighProfQualification());
        getJspContext().setAttribute("sdk_prof_qualification", trainingCourseWebModel.getSdkProfQualification());
        getJspContext().setAttribute("sec_prof_qualification", trainingCourseWebModel.getSecProfQualification());
        getJspContext().setAttribute("certificate_prof_qual", trainingCourseWebModel.getCertificateProfQualification());
        
        getJspContext().setAttribute("sec_caliber", trainingCourseWebModel.getSecCaliber());
        
        getJspContext().setAttribute("high_edu_level", trainingCourseWebModel.getHighEduLevel());
        
        getJspContext().setAttribute("document_type", trainingCourseWebModel.getDocumentType());
        getJspContext().setAttribute("document_number", trainingCourseWebModel.getDocumentNumber());
        getJspContext().setAttribute("document_date", trainingCourseWebModel.getDocumentDate());
        getJspContext().setAttribute("document_series", trainingCourseWebModel.getDocumentSeries());
        getJspContext().setAttribute("document_reg_num", trainingCourseWebModel.getDocumentRegNumber());
        
        getJspContext().setAttribute("sdk_document_type", trainingCourseWebModel.getSdkDocumentType());
        getJspContext().setAttribute("sdk_document_number", trainingCourseWebModel.getSdkDocumentNumber());
        getJspContext().setAttribute("sdk_document_date", trainingCourseWebModel.getSdkDocumentDate());
        getJspContext().setAttribute("sdk_document_series", trainingCourseWebModel.getSdkDocumentSeries());
        getJspContext().setAttribute("sdk_document_reg_num", trainingCourseWebModel.getSdkDocumentRegNumber());
      
        getJspContext().setAttribute("has_education", trainingCourseWebModel.isHasEducation());
        getJspContext().setAttribute("has_experience", trainingCourseWebModel.isHasExperience());
        getJspContext().setAttribute("education", trainingCourseWebModel.getEducationType());
        getJspContext().setAttribute("not_restricted", trainingCourseWebModel.isNotRestricted());
        
    }
}
//--------------------------------------------------------------------------
