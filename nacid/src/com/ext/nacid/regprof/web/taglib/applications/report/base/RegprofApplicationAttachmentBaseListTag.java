package com.ext.nacid.regprof.web.taglib.applications.report.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.base.AttachmentForReportBaseWebModel;
//RayaWritten---------------------------------------------------------------------
public class RegprofApplicationAttachmentBaseListTag extends SimpleTagSupport{
    private String attributePrefix = "";
    
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }
    public void doTag() throws JspException, IOException {
        String attributeName = WebKeys.REGPROF_APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL;
        attributeName = attributePrefix + attributeName;
        List<AttachmentForReportBaseWebModel> attachments = (List<AttachmentForReportBaseWebModel>) getJspContext().getAttribute(attributeName, PageContext.REQUEST_SCOPE);
        if (attachments != null) {
            //getJspContext().setAttribute("type", intType);
            for (AttachmentForReportBaseWebModel m:attachments) {
                getJspContext().setAttribute("document_type", m.getDocumentType());
                getJspContext().setAttribute("id", m.getId());
                getJspContext().setAttribute("file_name", m.getFileName());
                getJspBody().invoke(null);      
            }
        }
        
    }
}
//--------------------------------------------------------------------
