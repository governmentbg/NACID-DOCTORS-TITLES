package com.ext.nacid.web.taglib.applications.report.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.handlers.impl.applications.ExpertReportAttachmentsHandler;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.base.AttachmentForReportBaseWebModel;

/**
 * tozi klas shte se polzva za generirane na vsi4ki vidove preka4eni files svyrzani s reportite
 * @author ggeorgiev
 *
 */
public class ApplicationAttachmentsBaseListTag extends SimpleTagSupport {
	private String type;
	private String attributePrefix = "";
	public void setType(String type) {
		this.type = type;
	}
	public void setAttributePrefix(String attributePrefix) {
		this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
	}
	public void doTag() throws JspException, IOException {
		String attributeName;
		int intType;
		if ("applications".equals(type)) {
			attributeName = WebKeys.APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL;
			intType = ExpertReportAttachmentsHandler.ATTACHMENT_TYPE_APPLICANT_ATTACHMENTS;
		} else if ("univalidity".equals(type)) {
			attributeName = WebKeys.UNI_EXAM_ATTCH_WEB_MODEL;
			intType = ExpertReportAttachmentsHandler.ATTACHMENT_TYPE_UNI_EXAM_ATTACHMENTS;
		} else if ("diplexam".equals(type)) {
			attributeName = WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL;
			intType = ExpertReportAttachmentsHandler.ATTACHMENT_TYPE_DIPLOMA_EXAM_ATTACHMENTS;
		} else {
			throw new RuntimeException("no attribute name defined for type = " + type);
		}
		attributeName = attributePrefix + attributeName;
		List<AttachmentForReportBaseWebModel> attachments = (List<AttachmentForReportBaseWebModel>) getJspContext().getAttribute(attributeName, PageContext.REQUEST_SCOPE);
		if (attachments != null) {
			getJspContext().setAttribute("type", intType);
			for (AttachmentForReportBaseWebModel m:attachments) {
				getJspContext().setAttribute("document_type", m.getDocumentType());
				getJspContext().setAttribute("id", m.getId());
				getJspContext().setAttribute("file_name", m.getFileName());
				getJspBody().invoke(null);		
			}
		}
		
	}

}
