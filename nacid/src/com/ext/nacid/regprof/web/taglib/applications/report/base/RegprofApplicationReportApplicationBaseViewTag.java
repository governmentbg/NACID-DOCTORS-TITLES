package com.ext.nacid.regprof.web.taglib.applications.report.base;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.regprof.web.model.applications.report.base.RegprofApplicationForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofDocumentRecipientBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseForReportBaseWebModel;
import com.nacid.web.WebKeys;

//RayaWritten-------------------------------------------------------------------------------------------
public class RegprofApplicationReportApplicationBaseViewTag extends SimpleTagSupport{
    RegprofApplicationForReportBaseWebModel webmodel;
    protected String attributePrefix = "";
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }
    public void generateBaseData() {
        webmodel = (RegprofApplicationForReportBaseWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel == null) {
            return;
        }

        getJspContext().setAttribute("email", webmodel.getApplicantEmail());

        getJspContext().setAttribute("applicant_country", webmodel.getApplicantCountry());
        getJspContext().setAttribute("applicant_addr_details", webmodel.getApplicantAddrDetails());
        getJspContext().setAttribute("applicant_phone", webmodel.getApplicantPhone());
        getJspContext().setAttribute("applicant_city", webmodel.getApplicantCity());
        getJspContext().setAttribute("application_country", webmodel.getApplicationCountry());

        getJspContext().setAttribute("names_dont_match", webmodel.isNamesDontMatch());
        getJspContext().setAttribute("service_type", webmodel.getServiceType());
        
        getJspContext().setAttribute("show_application_attachments", getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE) == null ? false : true);
        getJspContext().setAttribute("is_personal_data_usage", webmodel.isPersonalDataUsage());

        if (webmodel.getApplicantDocument() != null) {
            getJspContext().setAttribute("applicant_document_number", webmodel.getApplicantDocument().getNumber());
            getJspContext().setAttribute("applicant_document_date", webmodel.getApplicantDocument().getDateOfIssue());
            getJspContext().setAttribute("applicant_document_issued_by", webmodel.getApplicantDocument().getIssuedBy());
            getJspContext().setAttribute("document_receive_method", webmodel.getDocumentReceiveMethod());
        }
        getJspContext().setAttribute("applicant_personal_id_document_type", webmodel.getApplicantPersonalIdDocumentType());
        RegprofTrainingCourseForReportBaseWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
        if (trainingCourseWebModel != null) {
            getJspContext().setAttribute("document_firstName", trainingCourseWebModel.getDocumentFname());
            getJspContext().setAttribute("document_middleName", trainingCourseWebModel.getDocumentSname());
            getJspContext().setAttribute("document_lastName", trainingCourseWebModel.getDocumentLname());
            getJspContext().setAttribute("document_civil_id_type", trainingCourseWebModel.getDocumentCivilIdType());
            getJspContext().setAttribute("document_civil_id", trainingCourseWebModel.getDocumentCivilId());
            
        }
        RegprofDocumentRecipientBaseWebModel documentRecipient = webmodel == null ? null : webmodel.getDocumentRecipient();
        if (documentRecipient != null) {
            getJspContext().setAttribute("document_recipient_data", true);
            getJspContext().setAttribute("document_recipient_name", documentRecipient.getName());
            getJspContext().setAttribute("document_recipient_city", documentRecipient.getCity());
            getJspContext().setAttribute("document_recipient_district", documentRecipient.getDistrict());
            getJspContext().setAttribute("document_recipient_post_code", documentRecipient.getPostCode());
            getJspContext().setAttribute("document_recipient_address", documentRecipient.getAddress());
            getJspContext().setAttribute("document_recipient_mobile_phone", documentRecipient.getMobilePhone());
            getJspContext().setAttribute("document_recipient_country", documentRecipient.getCountry());

        }
    }
}
//---------------------------------------------------------------------------------------------------------------
