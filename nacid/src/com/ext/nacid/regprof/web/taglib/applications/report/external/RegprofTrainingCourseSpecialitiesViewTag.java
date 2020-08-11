package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofTrainingCourseForReportWebModel;
import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofTrainingCourseSpecialitiesForReportWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseSpecialityForReportBaseWebModel;

import com.nacid.web.WebKeys;

//RayaWritten----------------------------------------------------------
public class RegprofTrainingCourseSpecialitiesViewTag extends SimpleTagSupport{
    ExtRegprofApplicationForReportWebModel webmodel;
    protected String attributePrefix = "";
    
    @Override
    public void doTag() throws JspException, IOException {        
        webmodel = (ExtRegprofApplicationForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel == null) {
            return;
        }
        ExtRegprofTrainingCourseForReportWebModel trainingCourseWebModel = (ExtRegprofTrainingCourseForReportWebModel) (webmodel == null ? null : webmodel.getTrainingCourseWebModel());
        if (trainingCourseWebModel != null) {
            for(RegprofTrainingCourseSpecialityForReportBaseWebModel baseSp: webmodel.getSpecialities()){
                ExtRegprofTrainingCourseSpecialitiesForReportWebModel sp = (ExtRegprofTrainingCourseSpecialitiesForReportWebModel) baseSp;
                if(trainingCourseWebModel.isHigh()){
                    getJspContext().setAttribute("high_speciality", sp.getHigherSpeciality());
                    getJspContext().setAttribute("high_speciality_txt", sp.getHigherSpecialityTxt());
                    getJspBody().invoke(null);
                } else if(trainingCourseWebModel.isSdk()){
                    getJspContext().setAttribute("sdk_speciality", sp.getSdkSpeciality());
                    getJspContext().setAttribute("sdk_speciality_txt", sp.getSdkSpecialityTxt());
                    getJspContext().setAttribute("high_speciality", sp.getHigherSpeciality());
                    getJspContext().setAttribute("high_speciality_txt", sp.getHigherSpecialityTxt());
                    getJspBody().invoke(null);
                } else if(trainingCourseWebModel.isSec()){
                    getJspContext().setAttribute("sec_speciality", sp.getSecondarySpeciality());
                    getJspContext().setAttribute("sec_speciality_txt", sp.getSecondarySpecialityTxt());
                    getJspBody().invoke(null);
                }
            }
        }
    }

    public ExtRegprofApplicationForReportWebModel getWebModel() {
        return webmodel;
    }
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }
}
//--------------------------------------------------------------------
