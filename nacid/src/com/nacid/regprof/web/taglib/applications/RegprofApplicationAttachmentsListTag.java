package com.nacid.regprof.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl;
import com.nacid.web.WebKeys;

public class RegprofApplicationAttachmentsListTag extends SimpleTagSupport {

    public void doTag() throws JspException, IOException {
        
        String operationType = (String) getJspContext().getAttribute(WebKeys.REGPROF_OPERATION_TYPE, PageContext.REQUEST_SCOPE);
        
        RegprofApplicationImpl regprofApplication = (RegprofApplicationImpl) getJspContext().getAttribute(RegprofApplicationImpl.class.getName(), PageContext.REQUEST_SCOPE);
        //PersonRecord applicant = null;
        //String header = "";
        /*if (regprofApplication != null) {
            applicant = regprofApplication.getApplicant();
            //header = applicant.getFullName()+ " " + "- Деловоден № " + regprofApplication.getApplicationDetails().getAppNum();
        }*/
        getJspContext().setAttribute("record_id", regprofApplication.getId());
        //getJspContext().setAttribute("application_header", header);
        getJspContext().setAttribute("operation", operationType);

        getJspBody().invoke(null);
    }

}
