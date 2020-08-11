package com.ext.nacid.regprof.web.taglib.applications.report.internal;

import com.ext.nacid.regprof.web.taglib.applications.report.base.RegprofApplicationReportApplicationBaseViewTag;
import com.nacid.regprof.web.model.applications.report.internal.RegprofApplicationInternalForReportWebModel;
import com.nacid.web.WebKeys;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
//RayaWritten-------------------------------------------------------------------------------------------
public class RegprofApplicationReportViewTag extends RegprofApplicationReportApplicationBaseViewTag{
    RegprofApplicationInternalForReportWebModel webmodel;

    @Override
    public void doTag() throws JspException, IOException {
        super.generateBaseData();
        
        webmodel = (RegprofApplicationInternalForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel == null) {
            return;
        }
        getJspContext().setAttribute("personal_email_informing", webmodel.isPersonalEmailInforming());
        getJspContext().setAttribute("show_representative", webmodel.isShowRepresentative());
        getJspContext().setAttribute("repPhone", webmodel.getRepPhone());
        getJspContext().setAttribute("repEmail", webmodel.getRepEmail());
        getJspContext().setAttribute("repAddressDetails", webmodel.getRepAddrDetails());
        getJspContext().setAttribute("show_repCompany", webmodel.isRepFromCompany());
        getJspContext().setAttribute("repCompany", webmodel.getRepresentativeCompany());
        getJspContext().setAttribute("application_status", webmodel.getApplicationStatus());
        getJspContext().setAttribute("application_status_for_applicant", webmodel.getApplicationStatusForApplicant());
        getJspContext().setAttribute("docflow_status", webmodel.getDocflowStatusName());
        getJspContext().setAttribute("archive_number", webmodel.getArchiveNumber());
        getJspContext().setAttribute("show_archive_number", webmodel.isShowArchiveNumber());
        
        getJspContext().setAttribute("recognized", webmodel.isRecognized());
        getJspContext().setAttribute("certificate_number", webmodel.getCertificateNumber());
        getJspContext().setAttribute("recognized_profession", webmodel.getRecognizedProfession());
        getJspContext().setAttribute("recognized_experience", webmodel.getRecognizedExperience());
        getJspContext().setAttribute("is_data_authentic", webmodel.isDataAuthentic());
        
        
        getJspBody().invoke(null);
    }

    public RegprofApplicationInternalForReportWebModel getWebModel() {
        return webmodel;
    }
}
//---------------------------------------------------------------------------------------------------------------
