package com.ext.nacid.regprof.web.taglib.applications.report.internal;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.ext.nacid.regprof.web.taglib.applications.report.base.RegprofApplicationReportTrainingCourseBaseViewTag;
import com.nacid.regprof.web.model.applications.report.internal.RegprofApplicationInternalForReportWebModel;
import com.nacid.regprof.web.model.applications.report.internal.RegprofTrainingCourseInternalForReportWebModel;
import com.nacid.web.WebKeys;

//RayaWritten-------------------------------------------------------------------------------------------
public class RegprofApplicationReportTrainingCourseViewTag extends RegprofApplicationReportTrainingCourseBaseViewTag{
    RegprofApplicationInternalForReportWebModel webmodel;
    public void doTag() throws JspException, IOException {
        super.generateBaseData();
        
        webmodel = (RegprofApplicationInternalForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        RegprofTrainingCourseInternalForReportWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
        /**
         * zapishe li se nov zapis za application 100% se syzdava i nov zapis za
         * TrainingCourse, taka 4e ne bi trqbvalo da ima situacii kogato tova neshto
         * da e null!!!!
         */
        if (trainingCourseWebModel == null) {
            return;
        }
        getJspBody().invoke(null);
       
    }
    public RegprofApplicationInternalForReportWebModel getWebmodel() {
        return webmodel;
    }
    
}
//-------------------------------------------------------------------------------------------
