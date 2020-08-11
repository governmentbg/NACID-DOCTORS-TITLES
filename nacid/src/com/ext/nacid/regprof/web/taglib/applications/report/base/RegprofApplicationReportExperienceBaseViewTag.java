package com.ext.nacid.regprof.web.taglib.applications.report.base;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.data.DataConverter;
import com.nacid.regprof.web.model.applications.report.base.RegprofApplicationForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofProfessionExperienceForReportBaseWebModel;
import com.nacid.web.WebKeys;

//RayaWritten-----------------------------------------------------
public class RegprofApplicationReportExperienceBaseViewTag extends SimpleTagSupport{
    protected String attributePrefix;
    RegprofApplicationForReportBaseWebModel webmodel;
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }
    protected void generateBaseData() {
        
        webmodel = (RegprofApplicationForReportBaseWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        RegprofProfessionExperienceForReportBaseWebModel experience = webmodel.getProfessionExperience();
        
        getJspContext().setAttribute("period", experience.getExperiencePeriod());
        getJspContext().setAttribute("profession", experience.getNomenclatureProfessionExperience());
        getJspContext().setAttribute("documents", experience.getDocuments());        
    }
}
//------------------------------------------------------------------
