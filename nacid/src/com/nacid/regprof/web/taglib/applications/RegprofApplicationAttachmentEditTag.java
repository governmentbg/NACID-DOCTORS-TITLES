package com.nacid.regprof.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.regprof.web.model.applications.RegprofApplicationAttachmentWebModel;

public class RegprofApplicationAttachmentEditTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        RegprofApplicationAttachmentWebModel webmodel = (RegprofApplicationAttachmentWebModel) 
        getJspContext().getAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL, PageContext.REQUEST_SCOPE);

        getJspContext().setAttribute("id", webmodel.getId());
        getJspContext().setAttribute("applicationId", webmodel.getApplicationId());
        getJspContext().setAttribute("docDescr", webmodel.getDocDescr());
        getJspContext().setAttribute("fileName", webmodel.getFileName());
        getJspContext().setAttribute("scannedFileName", webmodel.getScannedFileName());

        getJspContext().setAttribute("docflowNum", webmodel.getDocflowNum());
        getJspContext().setAttribute("docflowUrl", webmodel.getDocflowUrl());
        getJspContext().setAttribute("appStatusesToAllowCertNumberAssign", webmodel.getAppStatusesToAllowCertNumberAssign());

        getJspContext().setAttribute("backUrlApplAtt", getJspContext().getAttribute("backUrlApplAtt", PageContext.SESSION_SCOPE));

        getJspBody().invoke(null);
    }
}
