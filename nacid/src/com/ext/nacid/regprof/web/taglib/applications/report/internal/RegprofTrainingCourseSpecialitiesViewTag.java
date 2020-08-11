package com.ext.nacid.regprof.web.taglib.applications.report.internal;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseSpecialityForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.internal.RegprofApplicationInternalForReportWebModel;
import com.nacid.web.WebKeys;
//RayaWritten----------------------------------------------------------------------------
public class RegprofTrainingCourseSpecialitiesViewTag extends SimpleTagSupport{
    RegprofApplicationInternalForReportWebModel webmodel;
    protected String attributePrefix = "";
    
    @Override
    public void doTag() throws JspException, IOException {        
        webmodel = (RegprofApplicationInternalForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel == null) {
            return;
        }
        RegprofTrainingCourseForReportBaseWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
        if (webmodel.getSpecialities() != null && webmodel.getSpecialities().size() > 0) {
            for(RegprofTrainingCourseSpecialityForReportBaseWebModel sp: webmodel.getSpecialities()){
                if(trainingCourseWebModel.isHigh()){
                    getJspContext().setAttribute("high_speciality", sp.getHigherSpeciality());
                    getJspBody().invoke(null);
                } else if(trainingCourseWebModel.isSdk()){
                    getJspContext().setAttribute("sdk_speciality", sp.getSdkSpeciality());
                    getJspContext().setAttribute("high_speciality", sp.getHigherSpeciality());
                    getJspBody().invoke(null);
                } else if(trainingCourseWebModel.isSec()){
                    getJspContext().setAttribute("sec_speciality", sp.getSecondarySpeciality());
                    getJspBody().invoke(null);
                }
            }
        }
    }

    public RegprofApplicationInternalForReportWebModel getWebModel() {
        return webmodel;
    }
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }
}
//---------------------------------------------------------
