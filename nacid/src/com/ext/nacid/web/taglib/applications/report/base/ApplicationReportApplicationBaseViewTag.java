package com.ext.nacid.web.taglib.applications.report.base;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.applications.base.DocumentRecipientBase;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.base.DocumentRecipientBaseWebModel;
import com.nacid.web.model.applications.report.base.ApplicationForReportBaseWebModel;
import com.nacid.web.model.applications.report.base.TrainingCourseForReportBaseWebModel;

public class ApplicationReportApplicationBaseViewTag extends SimpleTagSupport {
	ApplicationForReportBaseWebModel webmodel;
	protected String attributePrefix = "";
	public void setAttributePrefix(String attributePrefix) {
		this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
	}
	public void generateBaseDate() {
		webmodel = (ApplicationForReportBaseWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
		if (webmodel == null) {
			return;
		}


		getJspContext().setAttribute("email", webmodel.getEmail());
		
		getJspContext().setAttribute("home_country", webmodel.getHomeCountry());
		getJspContext().setAttribute("home_city", webmodel.getHomeCity());
		getJspContext().setAttribute("home_post_code", webmodel.getHomePostCode());
		getJspContext().setAttribute("home_address_details", webmodel.getHomeAddressDetails());
		
		getJspContext().setAttribute("bg_city", webmodel.getBgCity());
		getJspContext().setAttribute("bg_post_code", webmodel.getBgPostCode());
		getJspContext().setAttribute("bg_address_details", webmodel.getBgAddressDetails());
		


		getJspContext().setAttribute("show_diploma_names", webmodel.showDiplomaNames());
		

		getJspContext().setAttribute("bgPhone", webmodel.getBgPhone());
		getJspContext().setAttribute("homePhone", webmodel.getHomePhone());
		
		getJspContext().setAttribute("recognition_purposes", webmodel.getRecognitionPurposes());
		getJspContext().setAttribute("show_application_attachments", getJspContext().getAttribute(attributePrefix + WebKeys.APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE) == null ? false : true);
		getJspContext().setAttribute("is_personal_data_usage", webmodel.isPersonalDataUsage());
		getJspContext().setAttribute("is_data_authentic", webmodel.isDataAuthentic());
		getJspContext().setAttribute("document_receive_method", webmodel.getDocumentReceiveMethod());
		getJspContext().setAttribute("doctorateApplication", webmodel.isDoctorateApplication());

		DocumentRecipientBaseWebModel documentRecipient = webmodel == null ? null : webmodel.getDocumentRecipient();
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


		/**
		 * adding data for diploma names.....
		 */
		TrainingCourseForReportBaseWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
		if (trainingCourseWebModel != null) {
			getJspContext().setAttribute("diploma_firstName", trainingCourseWebModel.getFirstName());
			getJspContext().setAttribute("diploma_middleName", trainingCourseWebModel.getSurName());
			getJspContext().setAttribute("diploma_lastName", trainingCourseWebModel.getLastName());
		}
	}

}
