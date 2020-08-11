package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.ext.nacid.regprof.web.taglib.applications.report.base.RegprofApplicationReportApplicationBaseViewTag;
import com.nacid.web.WebKeys;

//RayaWritten-----------------------------------------------------
public class RegprofApplicationReportViewTag extends RegprofApplicationReportApplicationBaseViewTag{
    ExtRegprofApplicationForReportWebModel webModel;
    @Override
    public void doTag()throws JspException, IOException {
        webModel = (ExtRegprofApplicationForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webModel == null) {
            return;
        }
        super.generateBaseData();
        getJspBody().invoke(null);
    }
    public ExtRegprofApplicationForReportWebModel getWebModel() {
        return webModel;
    }
    
}
//------------------------------------------------------------------
