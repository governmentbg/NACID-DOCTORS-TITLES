package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofProfessionExprienceForReportWebModel;
import com.ext.nacid.regprof.web.taglib.applications.report.base.RegprofApplicationReportExperienceBaseViewTag;
import com.nacid.web.WebKeys;

//RayaWritten-----------------------------------------------
public class RegprofApplicationReportExperienceViewTag extends RegprofApplicationReportExperienceBaseViewTag{
    ExtRegprofApplicationForReportWebModel webmodel;

    @Override
    public void doTag() throws JspException, IOException {
        webmodel = (ExtRegprofApplicationForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        ExtRegprofProfessionExprienceForReportWebModel experience = (ExtRegprofProfessionExprienceForReportWebModel) webmodel.getProfessionExperience();
        super.generateBaseData();
        getJspContext().setAttribute("profession_txt", experience.getProfessionExperienceTxt());
        getJspBody().invoke(null);
    }

    public ExtRegprofApplicationForReportWebModel getWebModel() {
        return webmodel;
    }
}
//--------------------------------------------------------------
