package com.ext.nacid.regprof.web.taglib.applications.report.internal;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.ext.nacid.regprof.web.taglib.applications.report.base.RegprofApplicationReportExperienceBaseViewTag;
import com.nacid.regprof.web.model.applications.report.internal.RegprofApplicationInternalForReportWebModel;
import com.nacid.web.WebKeys;


//RayaWritten---------------------------------------------------------------
public class RegprofApplicationReportExperienceViewTag extends RegprofApplicationReportExperienceBaseViewTag{
    RegprofApplicationInternalForReportWebModel webmodel;

    @Override
    public void doTag() throws JspException, IOException {
        webmodel = (RegprofApplicationInternalForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        super.generateBaseData();
        
        getJspBody().invoke(null);
    }

    public RegprofApplicationInternalForReportWebModel getWebModel() {
        return webmodel;
    }
}
//------------------------------------------------------------------------------