package com.nacid.regprof.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.regprof.web.model.applications.DocExamAttachmentWebModel;
import com.nacid.web.WebKeys;
//RayaWritten----------------------------------------------
public class DocExamAttachmentEditTag extends SimpleTagSupport{
    @Override
    public void doTag() throws JspException, IOException {
        DocExamAttachmentWebModel webmodel = (DocExamAttachmentWebModel) getJspContext()
            .getAttribute( WebKeys.DOC_EXAM_ATTCH_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
        
        getJspContext().setAttribute("id", webmodel.getId());
        getJspContext().setAttribute("docExamId", webmodel.getDocExamId());
        getJspContext().setAttribute("docDescr", webmodel.getDocDescr());
        getJspContext().setAttribute("fileName", webmodel.getFileName());
        getJspContext().setAttribute("applicationId", webmodel.getApplicationId());
        getJspContext().setAttribute("scannedFileName", webmodel.getScannedFileName());

        getJspContext().setAttribute("docflowNum", webmodel.getDocflowNum());
        getJspContext().setAttribute("docflowUrl", webmodel.getDocflowUrl());

        
        getJspContext().setAttribute("backUrlDiplExam", 
                getJspContext().getAttribute("backUrlDiplExam", PageContext.SESSION_SCOPE));
        
        
        getJspBody().invoke(null);
    }
}
//-----------------------------------------------------------------